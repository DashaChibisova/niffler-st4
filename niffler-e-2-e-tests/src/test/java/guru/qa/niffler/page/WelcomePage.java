package guru.qa.niffler.page;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byLinkText;
import static com.codeborne.selenide.Selenide.$;

public class WelcomePage extends BasePage<WelcomePage> {
    private static final String WELCOME_PAGE = "http://127.0.0.1:3000/";
    public static final String URL = CFG.frontUrl();

    private final SelenideElement loginButton = $("a[href*='redirect']");
    private final SelenideElement registerButton = $("a[href*='register']");

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

  @Step("Redirect to login page")
  public LoginPage doLogin() {
    loginButton.click();
    return new LoginPage();
  }

  @Step("Redirect to register page")
  public RegisterPage doRegister() {
    registerButton.click();
    return new RegisterPage();
  }

  @Step("Check that page is loaded")
  @Override
  public WelcomePage waitForPageLoaded() {
    loginButton.should(visible);
    registerButton.should(visible);
    return this;
  }
}
