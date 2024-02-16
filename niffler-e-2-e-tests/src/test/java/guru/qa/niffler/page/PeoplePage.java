package guru.qa.niffler.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.component.PeopleTable;
import io.qameta.allure.Step;

import static com.codeborne.selenide.ClickOptions.usingJavaScript;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.webdriver;
import static com.codeborne.selenide.WebDriverConditions.url;
import static guru.qa.niffler.page.BasePage.CFG;

public class PeoplePage {
    private static final String PEOPLE_PAGE = "http://127.0.0.1:3000/people";

    public static final String URL = CFG.frontUrl() + "/people";

    private final SelenideElement tableContainer = $(".people-content");
    private final PeopleTable table = new PeopleTable($(".table"));

    private final SelenideElement friendsTable = $(".people-content tbody");

    @Step("Проверить, что мы на нужной странице")
    public PeoplePage checkPageUrl() {
        webdriver().shouldHave(url(PEOPLE_PAGE));
        return this;
    }

    @Step("Проверить, что есть друзья")
    public PeoplePage checkHaveFriendsDsc() {
        friendsTable.$$("tr")
                .find(text("You are friends"))
                .shouldHave(appear);
        return this;
    }

    @Step("Проверить, что друг в статусе ожидания")
    public PeoplePage checkHavePendingInvitation() {
        friendsTable.$$("tr")
                .find(text("Pending invitation"))
                .shouldHave(appear);
        return this;
    }

    @Step("Проверить, что друга с таким именем нет в списке друзей")
    public SelenideElement checkNotHaveNameFriend(String name) {
        return friendsTable.$$("tr")
                .find(text(name))
                .shouldHave(disappear);
    }

    @Step("Check that the page is loaded")
    public PeoplePage waitForPageLoaded() {
        tableContainer.shouldBe(Condition.visible);
        return this;
    }

    @Step("Send invitation to user: {username}")
    public PeoplePage sendFriendInvitationToUser(String username) {
        SelenideElement friendRow = table.getRowByUsername(username);
        SelenideElement actionsCell = table.getActionsCell(friendRow);
        actionsCell.$(".button-icon_type_add")
                .click(usingJavaScript());
        return this;
    }

    @Step("Check invitation status for user: {username}")
    public PeoplePage checkInvitationSentToUser(String username) {
        SelenideElement friendRow = table.getRowByUsername(username);
        SelenideElement actionsCell = table.getActionsCell(friendRow);
        actionsCell.shouldHave(text("Pending invitation"));
        return this;
    }
}
