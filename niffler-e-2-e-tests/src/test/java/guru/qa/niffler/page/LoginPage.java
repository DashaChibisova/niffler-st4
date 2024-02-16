package guru.qa.niffler.page;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class LoginPage extends BasePage<LoginPage> {
    public static final String URL = CFG.authUrl() + "/login";

    private final SelenideElement usernameInput = $("input[name='username']");
    private final SelenideElement passwordInput = $("input[name='password']");
    private final SelenideElement submitButton = $("button[type='submit']");
    private final SelenideElement errorContainer = $(".form__error");

    private final SelenideElement loginInput = $("input[name='username']");
    private final SelenideElement submitBtn = $("button[type='submit']");

    @Step("Открыть страницу логина")
    public LoginPage open() {
        Selenide.open(URL);
        return this;
    }

    @Step("Ввести логин")
    public LoginPage setLogin(String login) {
        loginInput.setValue(login);
        return this;
    }

    @Step("Fill login page with credentials: username: {0}, password: {1}")
    public LoginPage fillLoginPage(String login, String password) {
        setUsername(login);
        setPassword(password);
        return this;
    }

    @Step("Set username: {0}")
    public LoginPage setUsername(String username) {
        usernameInput.setValue(username);
        return this;
    }

    @Step("Set password: {0}")
    public LoginPage setPassword(String password) {
        passwordInput.setValue(password);
        return this;
    }

    @Step("Submit login")
    public void submit() {
        submitButton.click();
    }

    @Step("Check error on page: {error}")
    public LoginPage checkError(String error) {
        errorContainer.shouldHave(text(error));
        return this;
    }

    @Step("Check that page is loaded")
    @Override
    public LoginPage waitForPageLoaded() {
        usernameInput.should(visible);
        passwordInput.should(visible);
        return this;
    }
}
