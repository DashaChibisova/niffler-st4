package guru.qa.niffler.po;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$;

public class LoginPage {
    private static final String MainPage = "http://127.0.0.1:3000/main";

    private SelenideElement redirect (){
        return $("a[href*='redirect']");
    }

    private SelenideElement inputUsername (){
        return $("input[name='username']");
    }

    private SelenideElement inputPassword (){
        return $("input[name='password']");
    }

    private SelenideElement buttonLogin (){
        return $("button[type='submit']");
    }

    public LoginPage open() {
        Selenide.open(MainPage);
        return this;
    }

    public LoginPage loginInSystem(String username, String password) {
        redirect().click();
        inputUsername().setValue(username);
        inputPassword().setValue(password);
        buttonLogin().click();
        return this;
    }
}

