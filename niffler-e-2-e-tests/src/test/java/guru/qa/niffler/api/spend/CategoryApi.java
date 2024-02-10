package guru.qa.niffler.api.spend;

import guru.qa.niffler.model.spend.CategoryJson;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

import java.util.List;

public interface CategoryApi {

    @POST("/category")
    Call<CategoryJson> addCategory(@Body CategoryJson category);

    @GET("/categories")
    public List<CategoryJson> getCategories(@Query("username") String username);
}
