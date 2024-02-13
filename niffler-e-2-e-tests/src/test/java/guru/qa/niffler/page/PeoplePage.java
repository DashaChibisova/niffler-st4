package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.webdriver;
import static com.codeborne.selenide.WebDriverConditions.url;

public class PeoplePage {
    private static final String PEOPLE_PAGE = "http://127.0.0.1:3000/people";

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
}
