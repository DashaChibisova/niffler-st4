package guru.qa.niffler.page.component;

import guru.qa.niffler.model.spend.SpendJson;

import static com.codeborne.selenide.Selenide.$;
import static guru.qa.niffler.condition.SpendCollectionCondition.spends;

public class SpendingTable extends BaseComponent<SpendingTable>{

  public SpendingTable() {
    super($(".spendings-table tbody"));
  }

  public SpendingTable checkSpends(SpendJson... expectedSpends) {
    getSelf().$$("tr").should(spends(expectedSpends));
    return this;
  }
}
