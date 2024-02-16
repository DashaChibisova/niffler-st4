package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.db.model.CurrencyValues;
import guru.qa.niffler.page.message.SuccessMsg;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.value;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class ProfilePage extends BasePage<ProfilePage> {
    public static final String URL = CFG.frontUrl() + "/profile";

    private final SelenideElement avatarInput = $("input[type='file']");
    private final SelenideElement avatar = $(".profile__avatar");

    //user data
    private final SelenideElement inputUsername = $("input[name='firstname']");
    private final SelenideElement inputSurname = $("input[name='surname']");
    private final SelenideElement submitBtn = $("button[type='submit']");
    private final SelenideElement userName = $(".avatar-container figcaption");
    private final SelenideElement nameInput = $("input[name='firstname']");
    private final SelenideElement surnameInput = $("input[name='surname']");
    private final SelenideElement categoryInput = $("input[name='category']");
    private final SelenideElement currencySelect = $("div .select-wrapper");
    private final SelenideElement submitButton = $(byText("Submit"));
    private final SelenideElement createCategoryButton = $(byText("Create"));
    private final ElementsCollection existingCategories = $$(".categories__list li ");

    //add new category
    private final SelenideElement inputCategory = $("input[name='category']");
    private final SelenideElement createBtn = $(byText("Create"));

    @Step("Fill profile page with rate: name: {0}, surname: {1}, currency: {2}")
    public ProfilePage fillProfile(String name, String surname, CurrencyValues currency) {
        setName(name);
        setSurname(surname);
        setCurrency(currency);
        submitProfile();
        return this;
    }

    @Step("Set name: {0}")
    public ProfilePage setName(String name) {
        nameInput.setValue(name);
        return this;
    }

    @Step("Set surname: {0}")
    public ProfilePage setSurname(String surname) {
        surnameInput.setValue(surname);
        return this;
    }

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

    @Step("Set currency: {0}")
    public ProfilePage setCurrency(CurrencyValues currency) {
        currencySelect.click();
        $$("div[id^='react-select']").find(exactText(currency.name())).click();
        return this;
    }

    @Step("Set name: {0}")
    public ProfilePage setAvatar(String photoInClasspath) {
        avatar.click();
        avatarInput.uploadFromClasspath(photoInClasspath);
        return this;
    }

    @Step("Set category: {0}")
    public ProfilePage addCategory(String category) {
        categoryInput.setValue(category);
        createCategoryButton.click();
        return this;
    }

    public ProfilePage checkCategoryExists(String category) {
        existingCategories.find(text(category)).shouldBe(visible);
        return this;
    }

    @Step("Check userName: {0}")
    public ProfilePage checkUsername(String username) {
        this.userName.should(text(username));
        return this;
    }

    @Step("Check name: {0}")
    public ProfilePage checkName(String name) {
        nameInput.shouldHave(value(name));
        return this;
    }

    @Step("Check surname: {0}")
    public ProfilePage checkSurname(String surname) {
        surnameInput.shouldHave(value(surname));
        return this;
    }

    @Step("Check currency: {0}")
    public ProfilePage checkCurrency(CurrencyValues currency) {
        currencySelect.shouldHave(text(currency.name()));
        return this;
    }

    @Step("Save profile")
    public ProfilePage submitProfile() {
        submitButton.click();
        return this;
    }

    @Step("Check that page is loaded")
    @Override
    public ProfilePage waitForPageLoaded() {
        userName.should(visible);
        return this;
    }
}
