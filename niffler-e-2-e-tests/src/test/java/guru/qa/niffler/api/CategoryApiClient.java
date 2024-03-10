package guru.qa.niffler.api;

import guru.qa.niffler.config.Config;

public class CategoryApiClient extends RestClient{
    private final CategoryApi categoryApi;

    public CategoryApiClient() {
        super(Config.getInstance().spendUrl());
        this.categoryApi = retrofit.create(CategoryApi.class);
    }
}
