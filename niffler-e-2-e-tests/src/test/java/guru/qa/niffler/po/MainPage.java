package guru.qa.niffler.po;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.page;

public class MainPage {

    private final SelenideElement spendingTable = $(".spendings-table tbody");
    private final SelenideElement buttonDeleteSpendingTable = $(byText("Delete selected"));
    private final SelenideElement buttonFriends = $("[data-tooltip-id=friends]");
    private final SelenideElement buttonPeople = $("[data-tooltip-id=people]");

    public MainPage selectSpendingElement(String spendDescription) {
        spendingTable.$$("tr")
                .find(text(spendDescription))
                .$("td")
                .click();
        return this;
    }

    public MainPage clickDeleteSelectedButton() {
        buttonDeleteSpendingTable.click();
        return this;
    }

    public MainPage checkSpendingElementDisappear() {
        spendingTable.$$("tr")
                .shouldHave(size(0));
        return this;
    }

    public FriendsPage goToFriendsPage() {
        buttonFriends.click();
        return new FriendsPage();
    }

    public PeoplePage goToPeoplePage() {
        buttonPeople.click();
        return new PeoplePage();
    }
}