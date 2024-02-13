package guru.qa.niffler.api.spend;

import guru.qa.niffler.model.currency.CurrencyValues;
import guru.qa.niffler.model.spend.SpendJson;
import guru.qa.niffler.model.spend.StatisticJson;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.Date;
import java.util.List;

public interface SpendApi {

  @POST("/addSpend")
  Call<SpendJson> addSpend(@Body SpendJson spend);


  @GET("/spends")
  public List<SpendJson> getSpends(@Query("username") String username,
                                   @Query("filterCurrency") CurrencyValues filterCurrency,
                                   @Query("dateFrom") Date from,
                                   @Query("dateTo") Date to);

  @GET("/statistic")
  public List<StatisticJson> getStatistic(@Query("username") String username,
                                          @Query("userCurrency") CurrencyValues userCurrency,
                                          @Query("filterCurrency") CurrencyValues filterCurrency,
                                          @Query("dateFrom") Date from,
                                          @Query("dateTo") Date to);

  @PATCH("/editSpend")
  public SpendJson editSpend(@Body SpendJson spend);

  @DELETE("/deleteSpends")
  public void deleteSpends(@Query("username") String username,
                           @Query("ids") List<String> ids);
}
