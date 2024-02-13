package guru.qa.niffler.api.userdata;

import guru.qa.niffler.api.RestClient;
import guru.qa.niffler.config.Config;

public class FriendsApiClient extends RestClient {

    private final FriendsApi friendsApi;

    public FriendsApiClient() {
        super(Config.getInstance().frontUrl());
        this.friendsApi = retrofit.create(FriendsApi.class);
    }
}
