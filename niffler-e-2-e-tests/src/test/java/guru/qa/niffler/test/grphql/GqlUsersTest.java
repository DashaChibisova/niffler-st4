package guru.qa.niffler.test.grphql;

import guru.qa.niffler.jupiter.annotation.*;
import guru.qa.niffler.jupiter.converter.GqlRequestConverter;
import guru.qa.niffler.model.gql.GqlRequest;
import guru.qa.niffler.model.gql.GqlUpdateUser;
import guru.qa.niffler.model.gql.GqlUser;
import guru.qa.niffler.model.gql.GqlUsers;
import guru.qa.niffler.model.userdata.FriendState;
import guru.qa.niffler.model.userdata.UserJson;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;


public class GqlUsersTest extends BaseGraphQLTest {

  @Test
  @ApiLogin(
      user = @TestUser()
  )
  void currentUserShouldBeReturned(@User UserJson testUser,
                                   @Token String bearerToken,
                                   @GqlRequestFile("gql/currentUserQuery.json") guru.qa.niffler.model.gql.GqlRequest request) throws Exception {

    final GqlUser response = gatewayGqlApiClient.currentUser(bearerToken, request);
    Assertions.assertEquals(
        testUser.username(),
        response.getData().getUser().getUsername()
    );
  }

  @Test
  @ApiLogin(
          user = @TestUser(friend = @Friend(count = 2))
  )
  void friendsUserShouldBeReturned(@User UserJson testUser,
                                   @Token String bearerToken,
                                   @GqlRequestFile("gql/getFriendsQuery.json") guru.qa.niffler.model.gql.GqlRequest request) throws Exception {

    final GqlUser response = gatewayGqlApiClient.getFriends(bearerToken, request);

    Assertions.assertEquals(
            FriendState.FRIEND,
            response.getData().getUser().getFriends().get(0).getFriendState()
    );
    Assertions.assertEquals(2, response.getData().getUser().getFriends().size());
  }

  @Test
  @ApiLogin(
          user = @TestUser(incomeInvitation = @IncomeInvitation)
  )
  void incomeInvitationFriendsUserShouldBeReturned(@User UserJson testUser,
                                      @Token String bearerToken,
                                      @GqlRequestFile("gql/getFriendsQuery.json") guru.qa.niffler.model.gql.GqlRequest request) throws Exception {

    final GqlUser response = gatewayGqlApiClient.getFriends(bearerToken, request);

    Assertions.assertEquals(
            FriendState.INVITE_RECEIVED,
            response.getData().getUser().getInvitations().get(0).getFriendState()
    );
    Assertions.assertEquals(1, response.getData().getUser().getInvitations().size());
  }

  @Test
  @ApiLogin(
          user = @TestUser()
  )
  void updateUserBeReturned(@User UserJson testUser,
                                @Token String bearerToken,
                                @GqlRequestFile("gql/updateUserMutation.json") guru.qa.niffler.model.gql.GqlRequest request) throws Exception {


    final GqlUpdateUser response = gatewayGqlApiClient.updateUser(bearerToken, request);
    Assertions.assertEquals("Pizzly", response.getData().getUpdateUser().getFirstname());
    Assertions.assertEquals("Pizzlyvich", response.getData().getUpdateUser().getSurname());
    Assertions.assertEquals("EUR", response.getData().getUpdateUser().getCurrency().toString());
    System.out.println(response);
  }

  @Test
  @ApiLogin(
          user = @TestUser(friend = @Friend(count=2))
  )
  void getUsersShouldBeReturned(@User UserJson testUser,
                                @Token String bearerToken,
                                @GqlRequestFile("gql/usersQuery.json") guru.qa.niffler.model.gql.GqlRequest request) throws Exception {

    final GqlUsers response = gatewayGqlApiClient.users(bearerToken, request);
    Assertions.assertEquals(12, response.getData().getUsers().size());
  }


  @Test
  @ParameterizedTest
  @ApiLogin(user = @TestUser)
  @CsvSource( {
          "gql/getFriends2FriedsSubQuery.json, Can`t fetch over 2 friends sub-queries",
          "gql/getFriends2InvitationsSubQuery.json, Can`t fetch over 2 invitations sub-queries"
  })
  void userWithFriendsMustReturnError(@GqlRequestFileConvertor GqlRequest request,
                                      String expectedError,
                                      @Token String bearerToken) throws Exception {

    final GqlUser response = gatewayGqlApiClient.getFriends(bearerToken, request);

    Assertions.assertEquals(1, response.getErrors().size());
    Assertions.assertEquals(expectedError, response.getErrors().get(0).message());
  }
}
