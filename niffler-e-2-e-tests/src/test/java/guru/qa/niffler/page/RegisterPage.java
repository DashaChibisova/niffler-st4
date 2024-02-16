package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Selenide.$;

public class RegisterPage extends BasePage<RegisterPage>{

    private final SelenideElement inputUsername = $("input[name='username']");
    private final SelenideElement inputPassword = $("input[name='password']");
    private final SelenideElement inputPasswordSubmit = $("input[name='passwordSubmit']");
    private final SelenideElement signUpBtn = $("button[type='submit']");

    @Step("Ввести имя")
    public RegisterPage inputUsername(String username) {
        inputUsername.setValue(username);
        return this;
    }

    @Step("Ввести пароль")
    public RegisterPage inputPassword(String password) {
        inputPassword.setValue(password);
        inputPasswordSubmit.setValue(password);
        return this;
    }

    @Step("Нажать на кнопку регистрации")
    public void submit() {
        signUpBtn.click();
    }

    @Override
    public RegisterPage waitForPageLoaded() {
        return null;
    }
}


