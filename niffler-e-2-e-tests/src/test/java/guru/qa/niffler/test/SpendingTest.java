package guru.qa.niffler.test;

import com.codeborne.selenide.Configuration;
import guru.qa.niffler.jupiter.GenerateCategory;
import guru.qa.niffler.jupiter.GenerateSpend;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.po.LoginPage;
import guru.qa.niffler.po.MainPage;
import org.junit.jupiter.api.Test;


public class SpendingTest {
    private final LoginPage loginPage = new LoginPage();
    private final MainPage mainPage = new MainPage();

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
        loginPage.open()
                .loginInSystem("duck", "12345");
        mainPage.selectSpendingElement(spend.description())
                .clickDeleteSelectedButton()
                .checkSpendingElementDisappear();
    }
}
