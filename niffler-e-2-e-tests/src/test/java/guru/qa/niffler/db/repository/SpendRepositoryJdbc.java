package guru.qa.niffler.db.repository;

import guru.qa.niffler.db.DataSourceProvider;
import guru.qa.niffler.db.Database;
import guru.qa.niffler.db.model.SpendEntity;

import javax.sql.DataSource;
import java.sql.*;
import java.util.UUID;

public class SpendRepositoryJdbc implements SpendRepository {

    private final DataSource spendDs = DataSourceProvider.INSTANCE.dataSource(Database.SPEND);

    @Override
    public SpendEntity create(SpendEntity spend) {
        try (Connection conn = spendDs.getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement categoryPs = conn.prepareStatement(
                    "INSERT INTO \"category\" " +
                            "(category, username) " +
                            "VALUES (?, ?)", PreparedStatement.RETURN_GENERATED_KEYS);
                 PreparedStatement spendPs = conn.prepareStatement(
                         "INSERT INTO \"spend\" " +
                                 "(username, currency, spend_date, amount, description, category_id ) " +
                                 "VALUES (?, ?, ?, ?, ?, ?)")
            ) {

                categoryPs.setString(1, spend.getCategory().getCategory());
                categoryPs.setString(2, spend.getUsername());

                categoryPs.executeUpdate();

                UUID categoryId;
                try (ResultSet keys = categoryPs.getGeneratedKeys()) {
                    if (keys.next()) {
                        categoryId = UUID.fromString(keys.getString("id"));
                    } else {
                        throw new IllegalStateException("Can`t find id");
                    }
                }

                spendPs.setString(1, spend.getUsername());
                spendPs.setString(2, spend.getCurrency().name());
                spendPs.setDate(3, (Date) spend.getSpendDate());
                spendPs.setDouble(4, spend.getAmount());
                spendPs.setString(5, spend.getDescription());
                spendPs.setObject(6, categoryId);

                spendPs.executeUpdate();
                conn.commit();
                spend.getCategory().setId(categoryId);
            } catch (Exception e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return spend;
    }
}
