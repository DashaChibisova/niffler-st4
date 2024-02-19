package guru.qa.niffler.condition;

import com.codeborne.selenide.CheckResult;
import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Driver;
import com.codeborne.selenide.impl.CollectionSource;
import guru.qa.niffler.db.model.CurrencyValues;
import guru.qa.niffler.model.spend.SpendJson;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SpendCollectionCondition {

    public static CollectionCondition spends(SpendJson... expectedSPends) {
        return new CollectionCondition() {

            @Nonnull
            @Override
            public CheckResult check(Driver driver, List<WebElement> elements) {
                if (elements.size() != expectedSPends.length) {
                    return CheckResult.rejected("Incorrect table size", elements);
                }
                for (var i = 0; i < expectedSPends.length; i++) {
                    WebElement element = elements.get(i);
                    List<WebElement> tds = element.findElements(By.cssSelector("td"));
                    boolean checkPassed = false;

                    Map<String, String> spendActual = new HashMap<>();
                    Map<String, String> spendExpected = new HashMap<>();

                    spendActual.put("category", tds.get(4).getText());
                    spendActual.put("currency", CurrencyValues.valueOf(tds.get(3).getText()).toString());
                    spendActual.put("amount", String.valueOf(Double.parseDouble(tds.get(2).getText())));
                    spendActual.put("description", tds.get(5).getText());

                    SpendJson expectedSPend = expectedSPends[i];

                    spendExpected.put("category", expectedSPend.category());
                    spendExpected.put("currency", expectedSPend.currency().toString());
                    spendExpected.put("amount", String.valueOf(expectedSPend.amount()));
                    spendExpected.put("description", expectedSPend.description());

                    if (spendActual.equals(spendExpected)) {
                        checkPassed = true;
                    }

                    if (checkPassed) {
                        return CheckResult.accepted();
                    } else {
                        String rejected = "Expected: " + spendExpected + "\nActual: " + spendActual;
                        return CheckResult.rejected("Incorrect spends content", rejected);
                    }
                }
                return super.check(driver, elements);
            }

            @Override
            public void fail(CollectionSource collection, CheckResult lastCheckResult, Exception cause, long timeoutMs) {
                throw new UnsupportedOperationException(lastCheckResult.getActualValue().toString());

            }

            @Override
            public boolean missingElementSatisfiesCondition() {
                return false;
            }
        };
    }
}
