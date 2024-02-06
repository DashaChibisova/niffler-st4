package guru.qa.niffler.db.repository;

import guru.qa.niffler.db.DataSourceProvider;
import guru.qa.niffler.db.Database;
import guru.qa.niffler.db.model.SpendEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.JdbcTransactionManager;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.support.TransactionTemplate;

import java.sql.*;
import java.util.UUID;

public class SpendRepositorySJdbc implements SpendRepository {

    private final TransactionTemplate spendTxt;
    private final JdbcTemplate spendTemplate;

    private final PasswordEncoder pe = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    public SpendRepositorySJdbc() {
        JdbcTransactionManager spendTm = new JdbcTransactionManager(
                DataSourceProvider.INSTANCE.dataSource(Database.SPEND)
        );

        this.spendTxt = new TransactionTemplate(spendTm);
        this.spendTemplate = new JdbcTemplate(spendTm.getDataSource());
    }

    @Override
    public SpendEntity create(SpendEntity spend) {
        KeyHolder kh = new GeneratedKeyHolder();
        return spendTxt.execute(status -> {
            spendTemplate.update(con -> {
                PreparedStatement categoryPs = con.prepareStatement(
                        "INSERT INTO \"category\" " +
                                "(category, username) " +
                                "VALUES (?, ?)",
                        PreparedStatement.RETURN_GENERATED_KEYS
                );
                categoryPs.setString(1, spend.getCategory().getCategory());
                categoryPs.setString(2, spend.getUsername());
                return categoryPs;
            }, kh);

            spend.getCategory().setId((UUID) kh.getKeys().get("id"));

            spendTemplate.update(con -> {
                PreparedStatement spendPs = con.prepareStatement(
                        "INSERT INTO \"spend\" " +
                                "(username, currency, spend_date, amount, description, category_id ) " +
                                "VALUES (?, ?, ?, ?, ?, ?)"
                );
                spendPs.setString(1, spend.getUsername());
                spendPs.setString(2, spend.getCurrency().name());
                spendPs.setDate(3, (Date) spend.getSpendDate());
                spendPs.setDouble(4, spend.getAmount());
                spendPs.setString(5, spend.getDescription());
                spendPs.setObject(6, spend.getCategory().getId());

                return spendPs;
            });

            return spend;
        });
    }
}
