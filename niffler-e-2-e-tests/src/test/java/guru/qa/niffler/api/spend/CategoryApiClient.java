package guru.qa.niffler.api.spend;

import guru.qa.niffler.api.RestClient;
import guru.qa.niffler.api.currency.CurrencyApi;
import guru.qa.niffler.config.Config;

public class CategoryApiClient extends RestClient {

    private final CurrencyApi currencyApi;

    public CategoryApiClient() {
        super(Config.getInstance().frontUrl());
        this.currencyApi = retrofit.create(CurrencyApi.class);
    }
}
