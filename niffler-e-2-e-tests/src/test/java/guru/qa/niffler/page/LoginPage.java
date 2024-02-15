package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Selenide.$;

public class LoginPage extends BasePage<LoginPage> {
  private static final String MAIN_PAGE = "http://127.0.0.1:3000/main";


  private final SelenideElement loginInput = $("input[name='username']");
  private final SelenideElement passwordInput = $("input[name='password']");
  private final SelenideElement submitBtn = $("button[type='submit']");

  @Step("Открыть страницу логина")
  public LoginPage open() {
    Selenide.open(MAIN_PAGE);
    return this;
  }
  @Step("Ввести логин")
  public LoginPage setLogin(String login) {
    loginInput.setValue(login);
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

  @Step("Ввести пароль")
  @Step("Set password: {0}")
  public LoginPage setPassword(String password) {
    passwordInput.setValue(password);
    return this;
  }

  @Step("Нажать продолжить")
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

  @Step("Залогиниться")
  public LoginPage login(String username, String password) {
    setLogin(username);
    setPassword(password);
    submit();
    return this;
  }
}
