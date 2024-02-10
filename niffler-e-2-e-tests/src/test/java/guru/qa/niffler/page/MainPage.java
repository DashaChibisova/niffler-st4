package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import java.util.Date;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class MainPage extends BasePage<MainPage> {

  private final SelenideElement friendsBtn = $("[data-tooltip-id=friends]");
  private final SelenideElement peopleBtn = $("[data-tooltip-id=people]");

  //Add new spending
  private final SelenideElement chooseSpendingCategory = $("#react-select-3-placeholder");
  private final ElementsCollection nameSpendingBtn  = $$("#react-select-3-listbox");
  private final SelenideElement setAmount = $("[name=amount]");
  private final SelenideElement calendarWrapper  = $(".calendar-wrapper");
  private final SelenideElement descriptionSpending  = $("[name=description]");
  private final SelenideElement addSpendingBtn  = $(byText("Add new spending"));

  //History of spendings
  private final SelenideElement spendingTable = $(".spendings-table tbody");
  private final SelenideElement deleteSpendingTableBtn = $(byText("Delete selected"));
  private final SelenideElement todayBtn  = $(byText("Today"));
  private final SelenideElement lastWeekBtn  = $(byText("Last week"));
  private final SelenideElement lastMonthBtn  = $(byText("Last month"));
  private final SelenideElement allTimeBtn  = $(byText("All time"));

  //statistic
  private final SelenideElement statistic  = $(".main-content__section-stats");


  @Step("Выбрать статью расходов")
  public MainPage selectSpendingElement(String spendDescription) {
    spendingTable.$$("tr")
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
    spendingTable.$$("tr")
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
    return  this;
  }
}
