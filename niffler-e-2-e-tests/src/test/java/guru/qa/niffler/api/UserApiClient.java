package guru.qa.niffler.api;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.model.userdata.UserJson;


public class UserApiClient extends RestClient {

    private final UserApi userApi;

    public UserApiClient() {
        super(Config.getInstance().userdataUrl());
        this.userApi = retrofit.create(UserApi.class);
    }

    public UserJson getCurrentUser(String username) throws Exception {
        return userApi.currentUser(username).execute().body();
    }
}
