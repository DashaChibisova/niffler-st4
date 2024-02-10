package guru.qa.niffler.test;

import com.codeborne.selenide.Configuration;
import guru.qa.niffler.jupiter.GenerateCategory;
import guru.qa.niffler.jupiter.annotation.GenerateSpend;
import guru.qa.niffler.model.currency.CurrencyValues;
import guru.qa.niffler.model.spend.SpendJson;
import org.junit.jupiter.api.Test;


public class SpendingTest extends BaseWebTest {


    static {
        Configuration.browserSize = "1980x1024";
    }

    @GenerateCategory(
            category = "Обучение",
            username = "duck"
    )
    @GenerateSpend(
            username = "duck",
            description = "QA.GURU Advanced 4",
            amount = 72500.00,
            currency = CurrencyValues.RUB
    )
    @Test
    void spendingShouldBeDeletedByButtonDeleteSpending(SpendJson spend) {
        welcomePage.open()
                .clickLogin();
        loginPage.login("duck", "12345");
        mainPage.selectSpendingElement(spend.description())
                .clickDeleteSelectedButton()
                .checkSpendingElementDisappear();
    }
}
