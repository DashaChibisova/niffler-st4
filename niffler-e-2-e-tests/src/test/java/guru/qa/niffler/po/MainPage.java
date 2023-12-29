package guru.qa.niffler.po;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

public class MainPage {

    private final SelenideElement spendingTable = $(".spendings-table tbody");
    private final SelenideElement buttonDeleteSpendingTable = $(byText("Delete selected"));

    public MainPage selectSpendingElement (String spendDescription) {
        spendingTable.$$("tr")
                .find(text(spendDescription))
                .$("td")
                .click();
    return this;
    }

    public MainPage clickDeleteSelectedButton () {
        buttonDeleteSpendingTable.click();
    return this;
    }

    public MainPage checkSpendingElementDisappear () {
        spendingTable.$$("tr")
                .shouldHave(size(0));
    return this;
    }
}