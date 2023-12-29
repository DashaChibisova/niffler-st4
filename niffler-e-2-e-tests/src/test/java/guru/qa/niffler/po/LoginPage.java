package guru.qa.niffler.po;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$;

public class LoginPage {
    private static final String MAIN_PAGE = "http://127.0.0.1:3000/main";

    private final SelenideElement redirect = $("a[href*='redirect']");
    private final SelenideElement inputUsername = $("input[name='username']");
    private final SelenideElement inputPassword = $("input[name='password']");
    private final SelenideElement buttonLogin = $("button[type='submit']");

    public LoginPage open() {
        Selenide.open(MAIN_PAGE);
        return this;
    }

    public LoginPage loginInSystem(String username, String password) {
        redirect.click();
        inputUsername.setValue(username);
        inputPassword.setValue(password);
        buttonLogin.click();
        return this;
    }
}

