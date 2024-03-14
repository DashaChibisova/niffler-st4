package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.jupiter.annotation.Friend;
import guru.qa.niffler.jupiter.annotation.IncomeInvitation;
import guru.qa.niffler.jupiter.annotation.OutcomeInvitation;
import guru.qa.niffler.jupiter.annotation.TestUser;
import guru.qa.niffler.model.userdata.UserJson;

public class RestCreteUserExtension extends CreateUserExtension {
  @Override
  public UserJson createUser(TestUser user) {
    return null;
  }

  @Override
  public UserJson createCategory(TestUser user, UserJson createdUser) {
    return null;
  }

  @Override
  public UserJson createSpend(TestUser user, UserJson createdUser) {
    return null;
  }

  @Override
  public UserJson createFriend(Friend friend, UserJson createdUser) {
    return null;
  }

  @Override
  public UserJson createIncomeInvitation(IncomeInvitation incomeInvitation, UserJson createdUser) {
    return null;
  }

  @Override
  public UserJson createOutcomeInvitation(OutcomeInvitation outcomeInvitation, UserJson createdUser) {
    return null;
  }

  @Override
  public UserJson updateUser(TestUser user, UserJson createdUser) {
    return null;
  }
}
