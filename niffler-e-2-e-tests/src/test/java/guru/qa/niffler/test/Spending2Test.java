package guru.qa.niffler.test;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.db.model.*;
import guru.qa.niffler.db.repository.SpendRepository;
import guru.qa.niffler.jupiter.SpendRepositoryExtension;
import guru.qa.niffler.jupiter.annotation.DbUser;
import guru.qa.niffler.model.CurrencyValues;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Calendar;


@ExtendWith(SpendRepositoryExtension.class)
public class Spending2Test extends BaseWebTest {

    private SpendRepository spendRepository;

    private SpendEntity spend;
    private CategoryEntity cat;

    @BeforeEach
    void createUser() {
        cat = new CategoryEntity();
        cat.setUsername("duck3348899");
        cat.setCategory("Обучение3348899");

        spend = new SpendEntity();
        spend.setUsername("duck3348899");
        spend.setSpendDate(new java.sql.Date(Calendar.getInstance().getTime().getTime()));
        spend.setCurrency(CurrencyValues.RUB);
        spend.setAmount(6000.00);
        spend.setDescription("GGGGGGGGG");
        spend.setCategory(cat);

        spendRepository.createInSpend(spend);
    }

    @AfterEach
    void removeUser() {
        spendRepository.deleteInSpendByCategoryId(cat.getId());
    }

    @DbUser()
    @Test
    void checkSpendRepository() {
        loginPage.open()
                .loginInSystem("duck", "12345");
        mainPage.selectSpendingElement(spend.getDescription())
                .clickDeleteSelectedButton()
                .checkSpendingElementDisappear();
    }
}
