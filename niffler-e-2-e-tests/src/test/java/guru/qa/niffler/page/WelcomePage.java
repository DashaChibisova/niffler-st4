package guru.qa.niffler.page;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byLinkText;
import static com.codeborne.selenide.Selenide.$;

public class WelcomePage extends BasePage<WelcomePage> {
    private static final String WELCOME_PAGE = "http://127.0.0.1:3000/";


    private final SelenideElement loginBtn = $(byLinkText("Login"));
    private final SelenideElement registerBtn = $(byLinkText("Register"));
    private final SelenideElement headerText = $(".main__header");

    @Step("Открыть WelcomePage")
    public WelcomePage open() {
        Selenide.open(WELCOME_PAGE);
        return this;
    }

    @Step("Нажать кнопку Логин")
    public void clickLogin() {
        loginBtn.click();
    }

    @Step("Нажать кнопку Регистрация")
    public void clickRegister() {
        registerBtn.click();
    }

    @Step("Проверить заголовок WelcomePage")
    public WelcomePage checkHeader() {
        headerText.shouldHave(text("Welcome to magic journey with Niffler. The coin keeper"));
        return this;
    }
}
