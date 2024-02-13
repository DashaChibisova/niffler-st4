package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

public class ProfilePage extends BasePage<ProfilePage> {

    //user data
    private final SelenideElement inputUsername = $("input[name='firstname']");
    private final SelenideElement inputSurname = $("input[name='surname']");
    private final SelenideElement submitBtn = $("button[type='submit']");

    //add new category
    private final SelenideElement inputCategory = $("input[name='category']");
    private final SelenideElement createBtn = $(byText("Create"));

    @Step("Ввести имя")
    public ProfilePage inputUsername(String username) {
        inputUsername.setValue(username);
        return this;
    }

    @Step("Ввести фамилию")
    public ProfilePage inputSurname(String surname) {
        inputSurname.setValue(surname);
        return this;
    }

    @Step("Кликнуть на сохрань")
    public ProfilePage submitBtn() {
        submitBtn.click();
        return this;
    }

    @Step("Ввести категорию")
    public ProfilePage inputCategory(String category) {
        inputCategory.setValue(category);
        return this;
    }

    @Step("Кликнуть создать")
    public ProfilePage createBtn() {
        createBtn.click();
        return this;
    }
}

