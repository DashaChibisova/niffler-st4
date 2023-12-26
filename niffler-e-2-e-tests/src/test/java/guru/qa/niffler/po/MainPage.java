package guru.qa.niffler.po;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

public class MainPage {

    private String spending = "tr";
    private String elementSpendingTable = "td";

    private SelenideElement spendingTable (){
        return $(".spendings-table tbody");
    }
    private SelenideElement buttonDeleteSpendingTable (){
        return $(byText("Delete selected"));
    }

    public MainPage selectSpendingElement (String spendDescription) {
        spendingTable()
                .$$(spending)
                .find(text(spendDescription))
                .$$(elementSpendingTable)
                .first()
                .click();
    return this;
    }

    public MainPage clickDeleteSelectedButton () {
        buttonDeleteSpendingTable().click();
    return this;
    }

    public MainPage checkSpendingElementDisappear () {
        spendingTable()
                .$$(spending)
                .shouldHave(size(0));
    return this;
    }
}