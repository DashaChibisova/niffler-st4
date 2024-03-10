package guru.qa.niffler.api;

import guru.qa.niffler.api.spend.SpendApi;
import guru.qa.niffler.config.Config;

import guru.qa.niffler.model.spend.CategoryJson;
import guru.qa.niffler.model.spend.SpendJson;

public class SpendApiClient extends RestClient {

    private final SpendApi spendApi;

    public SpendApiClient() {
        super(Config.getInstance().spendUrl());
        this.spendApi = retrofit.create(SpendApi.class);
    }

    public SpendJson addSpend(SpendJson spend) throws Exception {
        return spendApi.addSpend(spend).execute().body();
    }

    public CategoryJson addCategory(CategoryJson spend) throws Exception {
        return spendApi.addCategory(spend).execute().body();
    }
}
