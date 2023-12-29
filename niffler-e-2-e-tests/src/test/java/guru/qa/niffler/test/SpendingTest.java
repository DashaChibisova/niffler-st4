package guru.qa.niffler.test;

import com.codeborne.selenide.Configuration;
import guru.qa.niffler.jupiter.GenerateCategory;
import com.codeborne.selenide.Selenide;
import guru.qa.niffler.jupiter.DisabledByIssue;
import guru.qa.niffler.jupiter.GenerateSpend;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.po.LoginPage;
import guru.qa.niffler.po.MainPage;
import io.qameta.allure.Allure;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

public class SpendingTest extends BaseWebTest {
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

  @DisabledByIssue("74")
  @Test
  void spendingShouldBeDeletedByButtonDeleteSpending(SpendJson spend) {
    $(".spendings-table tbody")
        .$$("tr")
        .find(text(spend.description()))
        .$$("td")
        .first()
        .click();

    Allure.step("Delete spending", () -> $(byText("Delete selected"))
        .click());

    Allure.step("Check that spending was deleted", () -> {
      $(".spendings-table tbody")
          .$$("tr")
          .shouldHave(size(0));
    });
  }
}
