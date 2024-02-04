package guru.qa.niffler.jupiter;

import com.github.javafaker.Faker;
import guru.qa.niffler.db.model.*;
import guru.qa.niffler.db.repository.*;
import guru.qa.niffler.jupiter.annotation.GenerateSpend;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendJson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.*;

import java.lang.reflect.Method;
import java.util.*;

public class DatabaseSpendExtension extends SpendExtension  implements ParameterResolver, BeforeEachCallback, AfterTestExecutionCallback {
    @Override
    SpendJson create(SpendJson spend) {
        return null;
    }

    public static final ExtensionContext.Namespace NAMESPACE
            = ExtensionContext.Namespace.create(DatabaseSpendExtension.class);

    private final SpendRepositorySJdbc userRepository = new SpendRepositorySJdbc();
    private static final String spendKey = "spend";
//    private static final String categoryKey = "category";

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        List<Method> methods = new ArrayList<>();
        methods.add(context.getRequiredTestMethod());
        methods.addAll(Arrays.stream(context.getRequiredTestClass().getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(BeforeEach.class))
                .toList());

//        Map<String, Object> users = new HashMap<>();

        GenerateSpend spendData = methods.get(0).getAnnotation(GenerateSpend.class);
        GenerateCategory categoryData = methods.get(0).getAnnotation(GenerateCategory.class);
        SpendEntity userSpend;
        if (spendData != null && categoryData!= null) {

            CategoryEntity userCategory = new CategoryEntity();
            userCategory.setUsername(categoryData.username());
            userCategory.setCategory(categoryData.category());

             userSpend = new SpendEntity();
            userSpend.setUsername(spendData.username());
            userSpend.setSpendDate(new java.sql.Date(Calendar.getInstance().getTime().getTime()));
            userSpend.setCurrency(spendData.currency());
            userSpend.setAmount(spendData.amount());
            userSpend.setDescription(spendData.description());
            userSpend.setCategory(userCategory);

            SpendEntity created = userRepository.createInSpend(userSpend);

            context.getStore(NAMESPACE)
                    .put(spendKey, created);
        }
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter()
                .getType()
                .isAssignableFrom(SpendEntity.class);
    }

    @Override
    public SpendJson resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return extensionContext.getStore(DatabaseSpendExtension.NAMESPACE)
                .get(spendKey, SpendJson.class);
    }

    @Override
    public void afterTestExecution(ExtensionContext context) throws Exception {
        UUID idCategory = context.getStore(NAMESPACE).get(spendKey, SpendEntity.class).getCategory().getId();
        userRepository.deleteInSpendByCategoryId(idCategory);

    }
}
