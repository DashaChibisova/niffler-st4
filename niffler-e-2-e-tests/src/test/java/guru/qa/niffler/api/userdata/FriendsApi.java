package guru.qa.niffler.api.userdata;

import guru.qa.niffler.model.userdata.FriendJson;
import guru.qa.niffler.model.userdata.UserJson;

import retrofit2.http.*;

import java.util.List;


public interface FriendsApi {

    @GET("/friends")
    public List<UserJson> friends(@Query("username") String username,
                                  @Query("includePending") boolean includePending);

    @GET("/invitations")
    public List<UserJson> invitations(@Query("username") String username);

    @POST("/acceptInvitation")
    public List<UserJson> acceptInvitation(@Query("username") String username,
                                           @Body FriendJson invitation);

    @POST("/declineInvitation")
    public List<UserJson> declineInvitation(@Query("username") String username,
                                            @Body FriendJson invitation);

    @POST("/addFriend")
    public UserJson addFriend(@Query("username") String username,
                              @Body FriendJson friend);

    @DELETE("/removeFriend")
    public List<UserJson> removeFriend(@Query("username") String username,
                                       @Query("username") String friendUsername);
}
