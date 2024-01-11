package guru.qa.niffler.po;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.webdriver;
import static com.codeborne.selenide.WebDriverConditions.url;

public class FriendsPage {
    private static final String FRIENDS_PAGE = "http://127.0.0.1:3000/friends";

    private final SelenideElement friendsTable = $(".people-content tbody");

    public FriendsPage checkPageUrl() {
        webdriver().shouldHave(url(FRIENDS_PAGE));
        return this;
    }

    public FriendsPage checkHaveFriendsDsc() {
        friendsTable.$$("tr")
                .find(text("You are friends"))
                .shouldHave(appear);
        return this;
    }

    public FriendsPage checkNoFriendsDsc() {
        friendsTable.$$("tr")
                .find(text("There are no friends yet!"))
                .shouldHave(disappear);
        return this;
    }
}
