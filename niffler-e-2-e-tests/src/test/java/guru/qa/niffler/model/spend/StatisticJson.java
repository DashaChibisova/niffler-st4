package guru.qa.niffler.model.spend;

import com.fasterxml.jackson.annotation.JsonProperty;
import guru.qa.niffler.model.currency.CurrencyValues;

import java.util.Date;
import java.util.List;

public record StatisticJson(
        @JsonProperty("dateFrom")
        Date dateFrom,
        @JsonProperty("dateTo")
        Date dateTo,
        @JsonProperty("currency")
        CurrencyValues currency,
        @JsonProperty("total")
        Double total,
        @JsonProperty("userDefaultCurrency")
        CurrencyValues userDefaultCurrency,
        @JsonProperty("totalInUserDefaultCurrency")
        Double totalInUserDefaultCurrency,
        @JsonProperty("categoryStatistics")
        List<StatisticByCategoryJson> categoryStatistics
) {
}
