package guru.qa.niffler.jupiter;

import guru.qa.niffler.db.model.*;
import guru.qa.niffler.db.repository.*;
import guru.qa.niffler.jupiter.annotation.GenerateSpend;
import guru.qa.niffler.model.SpendJson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.*;

import java.lang.reflect.Method;
import java.util.*;

public class DatabaseSpendExtension extends SpendExtension implements ParameterResolver, BeforeEachCallback {

    public static final ExtensionContext.Namespace NAMESPACE
            = ExtensionContext.Namespace.create(DatabaseSpendExtension.class);

    private SpendRepositoryJdbc userRepository = new SpendRepositoryJdbc();

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        Method methods = context.getRequiredTestMethod();

        GenerateSpend spendData = methods.getAnnotation(GenerateSpend.class);
        GenerateCategory categoryData = methods.getAnnotation(GenerateCategory.class);

        if (spendData != null && categoryData != null) {

            SpendJson spendJson = new SpendJson(
                    null,
                    new java.sql.Date(Calendar.getInstance().getTime().getTime()),
                    categoryData.category(),
                    spendData.currency(),
                    spendData.amount(),
                    spendData.description(),
                    spendData.username());

            context.getStore(NAMESPACE)
                    .put(context.getUniqueId(), create(spendJson));

        }
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter()
                .getType()
                .isAssignableFrom(SpendJson.class);
    }

    @Override
    public SpendJson resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return extensionContext.getStore(DatabaseSpendExtension.NAMESPACE)
                .get(extensionContext.getUniqueId(), SpendJson.class);
    }

    @Override
    SpendJson create(SpendJson spend) {

        CategoryEntity userCategory = new CategoryEntity();
        userCategory.setUsername(spend.username());
        userCategory.setCategory(spend.category());

        SpendEntity userSpend = new SpendEntity();
        userSpend.setUsername(spend.username());
        userSpend.setSpendDate(new java.sql.Date(Calendar.getInstance().getTime().getTime()));
        userSpend.setCurrency(spend.currency());
        userSpend.setAmount(spend.amount());
        userSpend.setDescription(spend.description());
        userSpend.setCategory(userCategory);

        SpendEntity created = userRepository.create(userSpend);

        SpendJson spendJson = new SpendJson(
                created.getId(),
                created.getSpendDate(),
                created.getCategory().getId().toString(),
                created.getCurrency(),
                created.getAmount(),
                created.getDescription(),
                created.getUsername());

        return spendJson;
    }
}
