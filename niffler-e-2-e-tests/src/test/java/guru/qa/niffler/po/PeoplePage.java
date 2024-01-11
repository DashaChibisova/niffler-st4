package guru.qa.niffler.po;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.webdriver;
import static com.codeborne.selenide.WebDriverConditions.url;

public class PeoplePage {
    private static final String PEOPLE_PAGE = "http://127.0.0.1:3000/people";

    private final SelenideElement friendsTable = $(".people-content tbody");

    public PeoplePage checkPageUrl() {
        webdriver().shouldHave(url(PEOPLE_PAGE));
        return this;
    }

    public PeoplePage checkHaveFriendsDsc() {
        friendsTable.$$("tr")
                .find(text("You are friends"))
                .shouldHave(appear);
        return this;
    }

    public PeoplePage checkHavePendingInvitation() {
        friendsTable.$$("tr")
                .find(text("Pending invitation"))
                .shouldHave(appear);
        return this;
    }

    public SelenideElement checkNotHaveNameFriend(String name) {
        return friendsTable.$$("tr")
                .find(text(name))
                .shouldHave(disappear);
    }
}
