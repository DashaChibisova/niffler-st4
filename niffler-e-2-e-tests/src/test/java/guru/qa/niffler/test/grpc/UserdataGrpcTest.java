package guru.qa.niffler.test.grpc;
import guru.qa.grpc.niffler.grpc.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserdataGrpcTest extends BaseUserdataGrpcTest {


  @Test
  void checkGetCurrentUser() {
    final UserName request = UserName.newBuilder()
            .setUsername("duck")
            .build();
    final User response = blockingStubUser.getCurrentUser(request);

    assertEquals("duck", response.getUsername());
    assertEquals("duck7", response.getFirstname());
    assertTrue( response.getSurname().isEmpty());
    assertEquals(CurrencyValues.RUB, response.getCurrency());
    assertTrue(response.getPhoto().isEmpty());

  }

  @Test
  void checkPostUpdateUserInfo() {

    final User user = User.newBuilder()
            .setUsername("duck")
            .setFirstname("duck76")
            .setCurrency(CurrencyValues.RUB)
            .build();
    final User response = blockingStubUser.postUpdateUserInfo(user);

    assertEquals("duck", response.getUsername());
    assertEquals("duck76", response.getFirstname());
    assertTrue( response.getSurname().isEmpty());
    assertEquals(CurrencyValues.RUB, response.getCurrency());
    assertTrue(response.getPhoto().isEmpty()); //

  }

  @Test
  void checkGetAllUsers() {

    final UserName user = UserName.newBuilder()
            .setUsername("duck")
            .build();
    final UsersResponce response = blockingStubUser.getAllUsers(user);
    assertEquals(13, response.getUserList().size());
  }

  @Test
  void checkGetFriends() {

    final FrendRequest frendRequest = FrendRequest.newBuilder()
            .setUsername("duck")
            .setIncludePending(true)
            .build();
    final UsersResponce response = blockingStubUser.getFriends(frendRequest);
    final User user = response.getUser(0);

    assertEquals(1, response.getUserList().size());
    assertEquals("dima", user.getUsername());
    assertEquals(CurrencyValues.RUB, user.getCurrency());
    assertEquals(FriendState.FRIEND, user.getFriendState());
  }

  @Test
  void checkGetInvitations() {
    final UserName userName = UserName.newBuilder()
            .setUsername("bee")
            .build();
    final UsersResponce response = blockingStubUser.getInvitations(userName);
    final User user = response.getUser(0);

    assertEquals(1, response.getUserList().size());
    assertEquals("goose", user.getUsername());
    assertEquals(CurrencyValues.RUB, user.getCurrency());
    assertEquals(FriendState.INVITE_RECEIVED, user.getFriendState());
  }

  @Test
  void checkPostAcceptInvitation() {

    final Friend friend = Friend.newBuilder()
            .setUsername("parrot")
            .build();

    final InvitationRequest invitationRequest = InvitationRequest.newBuilder()
            .setUsername("3333666")
            .setInvitation(friend)
            .build();

    final UsersResponce response = blockingStubUser.postAcceptInvitation(invitationRequest);
    final User user = response.getUser(0);

    assertEquals(1, response.getUserList().size());
    assertEquals("parrot", user.getUsername());
    assertEquals(CurrencyValues.RUB, user.getCurrency());
    assertEquals(FriendState.FRIEND, user.getFriendState());
  }

  @Test
  void checkPostDeclineInvitation() {

    final Friend friend = Friend.newBuilder()
            .setUsername("parrot")
            .build();

    final InvitationRequest invitationRequest = InvitationRequest.newBuilder()
            .setUsername("3333")
            .setInvitation(friend)
            .build();

    final UsersResponce response = blockingStubUser.postDeclineInvitation(invitationRequest);
    assertTrue(response.getUserList().isEmpty());

  }

  @Test
  void checkPostAddFriend() {

    final Friend friend = Friend.newBuilder()
            .setUsername("valentin_153")
            .build();

    final InvitationRequest invitationRequest = InvitationRequest.newBuilder()
            .setUsername("3333666")
            .setInvitation(friend)
            .build();

    final User user = blockingStubUser.postAddFriend(invitationRequest);

    assertEquals("valentin_153", user.getUsername());
    assertEquals(CurrencyValues.RUB, user.getCurrency());
    assertEquals(FriendState.INVITE_SENT, user.getFriendState());
  }

  @Test
  void checkDeleteRemoveFriend() {

    final RemoveFriendRequest removeFriendRequest = RemoveFriendRequest.newBuilder()
            .setUsername("lion")
            .setUsername("parrot")
            .build();

    final UsersResponce usersResponce = blockingStubUser.deleteRemoveFriend(removeFriendRequest);
    User user = usersResponce.getUserList().get(0);

    assertEquals("lion", user.getUsername());
    assertEquals(CurrencyValues.RUB, user.getCurrency());
    assertEquals(FriendState.INVITE_SENT, user.getFriendState());
  }
}
