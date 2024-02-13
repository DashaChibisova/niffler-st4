package guru.qa.niffler.test;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.jupiter.GenerateCategory;
import guru.qa.niffler.jupiter.annotation.GenerateSpend;
import guru.qa.niffler.model.currency.CurrencyValues;
import guru.qa.niffler.model.spend.SpendJson;
import guru.qa.niffler.page.MainPage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;


public class SpendingTest extends BaseWebTest {

  @BeforeEach
  void doLogin() {
    Selenide.open("http://127.0.0.1:3000/main");
    $("a[href*='redirect']").click();
    $("input[name='username']").setValue("duck");
    $("input[name='password']").setValue("12345");
    $("button[type='submit']").click();
  }

    @GenerateCategory(
            category = "Обучение1",
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
    $(".spendings-table tbody")
            .$$("tr")
            .find(text(spend.description()))
            .$$("td")
            .first()
            .click();

    new MainPage()
            .getSpendingTable()
            .checkSpends(spend);

//    Allure.step("Delete spending", () -> $(byText("Delete selected"))
//        .click());
//
//    Allure.step("Check that spending was deleted", () -> {
//      $(".spendings-table tbody")
//          .$$("tr")
//          .shouldHave(size(0));
//    });
  }
}
