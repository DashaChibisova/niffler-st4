package guru.qa.niffler.page;

import com.codeborne.selenide.Selenide;
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
    return this;
  }

  @Step("Ввести пароль")
  public LoginPage setPassword(String password) {
    passwordInput.setValue(password);
    return this;
  }

  @Step("Нажать продолжить")
  public void submit() {
    submitBtn.click();
  }

  @Step("Залогиниться")
  public LoginPage login(String username, String password) {
    setLogin(username);
    setPassword(password);
    submit();
    return this;
  }
}
