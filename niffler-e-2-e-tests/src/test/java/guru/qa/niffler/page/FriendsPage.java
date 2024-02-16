package guru.qa.niffler.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.model.userdata.UserJson;
import guru.qa.niffler.page.component.PeopleTable;
import io.qameta.allure.Step;

import java.util.List;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.webdriver;
import static com.codeborne.selenide.WebDriverConditions.url;

public class FriendsPage extends BasePage<FriendsPage> {
    private static final String FRIENDS_PAGE = "http://127.0.0.1:3000/friends";
    public static final String URL = CFG.frontUrl() + "/friends";

    private final SelenideElement tableContainer = $(".people-content");
    private final PeopleTable table = new PeopleTable($(".table"));

    private final SelenideElement friendsTable = $(".people-content tbody");

    @Step("Окрылся урл со страницей друзей")
    public FriendsPage checkPageUrl() {
        webdriver().shouldHave(url(FRIENDS_PAGE));
        return this;
    }

    @Step("Проверить, что есть друзья")
    public FriendsPage checkHaveFriendsDsc() {
        friendsTable.$$("tr")
                .find(text("You are friends"))
                .shouldHave(appear);
        return this;
    }

    @Step("Проверить, что друзей нет")
    public FriendsPage checkNoFriendsDsc() {
        friendsTable.$$("tr")
                .find(text("There are no friends yet!"))
                .shouldHave(disappear);
        return this;
    }

  @Step("Check that the page is loaded")
  @Override
  public FriendsPage waitForPageLoaded() {
    tableContainer.shouldBe(Condition.visible);
    return this;
  }

  @Step("Check that friends count is equal to {expectedCount}")
  public FriendsPage checkExistingFriendsCount(int expectedCount) {
    table.getAllRows().shouldHave(size(expectedCount));
    return this;
  }

  @Step("Check that friends list contains data {0}")
  public FriendsPage checkExistingFriends(List<UserJson> expectedFriends) {
    table.getAllRows().filter(text("You are friends"));
    //TODO not implemented
    // .shouldHave(users(expectedFriends));
    return this;
  }

  @Step("Delete user from friends: {username}")
  public FriendsPage removeFriend(String username) {
    SelenideElement friendRow = table.getRowByUsername(username);
    SelenideElement actionsCell = table.getActionsCell(friendRow);
    actionsCell.$(".button-icon_type_close")
        .click();
    return this;
  }

  @Step("Accept invitation from user: {username}")
  public FriendsPage acceptFriendInvitationFromUser(String username) {
    SelenideElement friendRow = table.getRowByUsername(username);
    SelenideElement actionsCell = table.getActionsCell(friendRow);
    actionsCell.$(".button-icon_type_submit")
        .click();
    return this;
  }
}
