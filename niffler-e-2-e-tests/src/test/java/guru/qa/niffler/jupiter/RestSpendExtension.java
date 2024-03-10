package guru.qa.niffler.jupiter;

import guru.qa.niffler.api.spend.SpendApi;
import guru.qa.niffler.jupiter.annotation.GenerateSpend;
import guru.qa.niffler.jupiter.extension.SpendExtension;
import guru.qa.niffler.model.spend.SpendJson;
import okhttp3.OkHttpClient;
import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.io.IOException;
import java.util.Date;
import java.util.Optional;

public class RestSpendExtension extends SpendExtension implements BeforeEachCallback {

    public static final ExtensionContext.Namespace NAMESPACE
            = ExtensionContext.Namespace.create(RestSpendExtension.class);

    private static final OkHttpClient httpClient = new OkHttpClient.Builder().build();
    private static final Retrofit retrofit = new Retrofit.Builder()
            .client(httpClient)
            .baseUrl("http://127.0.0.1:8093")
            .addConverterFactory(JacksonConverterFactory.create())
            .build();

    private final SpendApi spendApi = retrofit.create(SpendApi.class);

    @Override
    public void beforeEach(ExtensionContext extensionContext) throws Exception {
        Optional<GenerateSpend> spend = AnnotationSupport.findAnnotation(
                extensionContext.getRequiredTestMethod(),
                GenerateSpend.class
        );

        if (spend.isPresent()) {
            GenerateSpend spendData = spend.get();
            Optional<GenerateCategory> category = AnnotationSupport.findAnnotation(
                    extensionContext.getRequiredTestMethod(),
                    GenerateCategory.class
            );
            GenerateCategory categoryData = category.get();

            SpendJson spendJson = new SpendJson(
                    null,
                    new Date(),
                    categoryData.category(),
                    spendData.currency(),
                    spendData.amount(),
                    spendData.description(),
                    spendData.username()
            );

            SpendJson created = spendApi.addSpend(spendJson).execute().body();
            extensionContext.getStore(NAMESPACE)
                    .put(extensionContext.getUniqueId(), created);
        }
    }

    protected SpendJson create(SpendJson spend) {
        try {
            return spendApi.addSpend(spend).execute().body();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
