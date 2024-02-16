package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.component.Footer;
import guru.qa.niffler.page.component.Header;
import guru.qa.niffler.page.component.Select;
import guru.qa.niffler.page.component.SpendingTable;
import guru.qa.niffler.page.component.Calendar;

import io.qameta.allure.Step;

import java.util.Date;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class MainPage extends BasePage<MainPage> {
    public static final String URL = CFG.frontUrl() + "/main";

    private final SelenideElement friendsBtn = $("[data-tooltip-id=friends]");
    private final SelenideElement peopleBtn = $("[data-tooltip-id=people]");

    //Add new spending
    private final SelenideElement chooseSpendingCategory = $("#react-select-3-placeholder");
    private final ElementsCollection nameSpendingBtn = $$("#react-select-3-listbox");
    private final SelenideElement setAmount = $("[name=amount]");
    private final SelenideElement calendarWrapper = $(".calendar-wrapper");
    private final SelenideElement descriptionSpending = $("[name=description]");
    private final SelenideElement addSpendingBtn = $(byText("Add new spending"));

    //History of spendings
    private final SelenideElement spendingTableS = $(".spendings-table tbody");
    private final SelenideElement deleteSpendingTableBtn = $(byText("Delete selected"));
    private final SelenideElement todayBtn = $(byText("Today"));
    private final SelenideElement lastWeekBtn = $(byText("Last week"));
    private final SelenideElement lastMonthBtn = $(byText("Last month"));
    private final SelenideElement allTimeBtn = $(byText("All time"));

    //statistic
    private final SelenideElement statistic = $(".main-content__section-stats");

    private final SelenideElement avatar = $(".header__avatar");
    private final SelenideElement statistics = $(".main-content__section.main-content__section-stats");
    private final SpendingTable spendingTable = new SpendingTable();

    protected final Header header = new Header();
    protected final Footer footer = new Footer();
    private final SelenideElement addSpendingSection = $(".main-content__section-add-spending");
    private final Select categorySelect = new Select(addSpendingSection.$("div.select-wrapper"));
    private final Calendar calendar = new Calendar(addSpendingSection.$(".react-datepicker"));
    private final SelenideElement amountInput = addSpendingSection.$("input[name='amount']");
    private final SelenideElement descriptionInput = addSpendingSection.$("input[name='description']");
    private final SelenideElement submitNewSpendingButton = addSpendingSection.$("button[type='submit']");
    private final SelenideElement errorContainer = addSpendingSection.$(".form__error");

    @Step("Выбрать статью расходов")
    public MainPage selectSpendingElement(String spendDescription) {
        spendingTableS.$$("tr")
                .find(text(spendDescription))
                .$("td")
                .click();
        return this;
    }

    @Step("Удалить статью расходов")
    public MainPage clickDeleteSelectedButton() {
        deleteSpendingTableBtn.click();
        return this;
    }

    @Step("Проверить, что расход удалился")
    public MainPage checkSpendingElementDisappear() {
        spendingTableS.$$("tr")
                .shouldHave(size(0));
        return this;
    }

    @Step("Перейти на страницу друзей")
    public void goToFriendsPage() {
        friendsBtn.click();
    }

    @Step("Перейти на страницу людей")
    public void goToPeoplePage() {
        peopleBtn.click();
    }

    @Step("Выбрать категорию")
    public MainPage chooseSpendingCategory(String nameCategory) {
        chooseSpendingCategory.click();
        nameSpendingBtn.findBy(text(nameCategory)).click();
        return this;
    }

    @Step("Ввести сумму")
    public MainPage setAmount(String amount) {
        setAmount.setValue(amount);
        return this;
    }

    @Step("Ввести дату")
    public MainPage inputDate(Date date) {
        calendarWrapper.setValue(String.valueOf(date));
        return this;
    }

    @Step("Ввести описание")
    public MainPage inputDescription(String description) {
        descriptionSpending.setValue(description);
        return this;
    }

    @Step("Нажать добавить трату")
    public void clickAddSpending() {
        addSpendingBtn.click();
    }

    @Step("Нажать на филтр сегодня")
    public void clickTodayBtn() {
        todayBtn.click();
    }

    @Step("Нажать на фильтр прошлая неделя")
    public void clickLastWeekBtn() {
        lastWeekBtn.click();
    }

    @Step("Нажать на фильтр прошлый месяц")
    public void clickLastMonthBtn() {
        lastMonthBtn.click();
    }

    @Step("Выбрать траты за все вермя")
    public void clickAllTimeBtn() {
        allTimeBtn.click();
    }

    @Step("Проверить, что статистика отобразилась")
    public MainPage checkThatStatisticDisplayed() {
        statistic.should(visible);
        return this;
    }

    public Header getHeader() {
        return header;
    }

    public Footer getFooter() {
        return footer;
    }

    public SpendingTable getSpendingTable() {
        spendingTable.getSelf().scrollIntoView(true);
        return spendingTable;
    }

    @Step("Check that page is loaded")
    @Override
    public MainPage waitForPageLoaded() {
        header.getSelf().should(visible).shouldHave(text("Niffler. The coin keeper."));
        footer.getSelf().should(visible).shouldHave(text("Study project for QA Automation Advanced. 2023"));
        spendingTable.getSelf().should(visible).shouldHave(text("History of spendings"));
        return this;
    }

    @Step("Select new spending category: {0}")
    public MainPage setNewSpendingCategory(String category) {
        Selenide.sleep(1000);
        categorySelect.setValue(category);
        return this;
    }

    @Step("Set new spending amount: {0}")
    public MainPage setNewSpendingAmount(int amount) {
        amountInput.setValue(String.valueOf(amount));
        return this;
    }

    @Step("Set new spending amount: {0}")
    public MainPage setNewSpendingAmount(double amount) {
        amountInput.setValue(String.valueOf(amount));
        return this;
    }

    @Step("Set new spending date: {0}")
    public MainPage setNewSpendingDate(Date date) {
        calendar.selectDateInCalendar(date);
        addSpendingSection.$(byText("Add new spending")).click();
        return this;
    }

    @Step("Set new spending description: {0}")
    public MainPage setNewSpendingDescription(String description) {
        descriptionInput.setValue(description);
        return this;
    }

    @Step("Click submit button to create new spending")
    public MainPage submitNewSpending() {
        submitNewSpendingButton.click();
        return this;
    }

    @Step("Check error: {0} is displayed")
    public MainPage checkError(String error) {
        errorContainer.shouldHave(text(error));
        return this;
    }
}
