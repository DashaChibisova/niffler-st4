package guru.qa.niffler.api.userdata;

import guru.qa.niffler.model.userdata.UserJson;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

import java.util.List;

public interface UserApi {

    @POST("/updateUserInfo")
    public UserJson updateUserInfo(@Body UserJson user);

    @GET("/currentUser")
    public UserJson currentUser(@Query("username") String username);

    @GET("/allUsers")
    public List<UserJson> allUsers(@Query("username") String username);
}
