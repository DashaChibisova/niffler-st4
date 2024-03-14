package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.db.model.Authority;
import guru.qa.niffler.db.model.AuthorityEntity;
import guru.qa.niffler.db.model.CurrencyValues;
import guru.qa.niffler.db.model.UserAuthEntity;
import guru.qa.niffler.db.model.UserEntity;
import guru.qa.niffler.db.repository.UserRepository;
import guru.qa.niffler.db.repository.UserRepositoryHibernate;
import guru.qa.niffler.jupiter.annotation.Friend;
import guru.qa.niffler.jupiter.annotation.IncomeInvitation;
import guru.qa.niffler.jupiter.annotation.OutcomeInvitation;
import guru.qa.niffler.jupiter.annotation.TestUser;
import guru.qa.niffler.model.TestData;
import guru.qa.niffler.model.userdata.FriendJson;
import guru.qa.niffler.model.userdata.FriendState;
import guru.qa.niffler.model.userdata.UserJson;
import guru.qa.niffler.utils.DataUtils;


import java.util.Arrays;

public class DataBaseCreteUserExtension extends CreateUserExtension {

  private static UserRepository userRepository = new UserRepositoryHibernate();

  @Override
  public UserJson createUser(TestUser user) {
    return createUserJson(user.username(),user.password());

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
    if(!friend.fake()) {
      UserJson friendUser = createUserJson("", "");
      UserEntity userEntity = userRepository.addFriends(createdUser.id(), friendUser.id());
      return new UserJson(
              userEntity.getId(),
              userEntity.getUsername(),
              userEntity.getFirstname(),
              userEntity.getSurname(),
              guru.qa.niffler.model.currency.CurrencyValues.valueOf(userEntity.getCurrency().name()),
              userEntity.getPhoto() == null ? "" : new String(userEntity.getPhoto()),
              null,
              new TestData(
                      friendUser.testData().password(),
                      null
              )
      );
    }
    return null;
  }

  @Override
  public UserJson createIncomeInvitation(IncomeInvitation incomeInvitation, UserJson createdUser) {
    if(!incomeInvitation.fake()) {
      UserJson friendUser = createUserJson("", "");
      UserEntity userEntity = userRepository.incomeInvitation(createdUser.id(), friendUser.id());
      return new UserJson(
              userEntity.getId(),
              userEntity.getUsername(),
              userEntity.getFirstname(),
              userEntity.getSurname(),
              guru.qa.niffler.model.currency.CurrencyValues.valueOf(userEntity.getCurrency().name()),
              userEntity.getPhoto() == null ? "" : new String(userEntity.getPhoto()),
              null,
              new TestData(
                      friendUser.testData().password(),
                      null
              )
      );
    }
    return null;
  }

  @Override
  public UserJson createOutcomeInvitation(OutcomeInvitation outcomeInvitation, UserJson createdUser) {
    if(!outcomeInvitation.fake()) {
      UserJson friendUser = createUserJson("", "");
      UserEntity userEntity = userRepository.incomeInvitation(createdUser.id(), friendUser.id());
      return new UserJson(
              userEntity.getId(),
              userEntity.getUsername(),
              userEntity.getFirstname(),
              userEntity.getSurname(),
              guru.qa.niffler.model.currency.CurrencyValues.valueOf(userEntity.getCurrency().name()),
              userEntity.getPhoto() == null ? "" : new String(userEntity.getPhoto()),
              null,
              new TestData(
                      friendUser.testData().password(),
                      null
              )
      );
    }
    return null;
  }

  @Override
  public UserJson updateUser(TestUser user, UserJson createdUser) {

    return null;
  }

  private UserJson createUserJson(String password, String username) {
    String usernameNew = username.isEmpty()
            ? DataUtils.generateRandomUsername()
            : username;
    String passwordNew = password.isEmpty()
            ? "12345"
            : password;

    UserAuthEntity userAuth = new UserAuthEntity();
    userAuth.setUsername(usernameNew);
    userAuth.setPassword(passwordNew);
    userAuth.setEnabled(true);
    userAuth.setAccountNonExpired(true);
    userAuth.setAccountNonLocked(true);
    userAuth.setCredentialsNonExpired(true);
    AuthorityEntity[] authorities = Arrays.stream(Authority.values()).map(
            a -> {
              AuthorityEntity ae = new AuthorityEntity();
              ae.setAuthority(a);
              return ae;
            }
    ).toArray(AuthorityEntity[]::new);

    userAuth.addAuthorities(authorities);

    UserEntity userEntity = new UserEntity();
    userEntity.setUsername(usernameNew);
    userEntity.setCurrency(CurrencyValues.RUB);

    userRepository.createInAuth(userAuth);
    userRepository.createInUserdata(userEntity);
    return new UserJson(
            userEntity.getId(),
            userEntity.getUsername(),
            userEntity.getFirstname(),
            userEntity.getSurname(),
            guru.qa.niffler.model.currency.CurrencyValues.valueOf(userEntity.getCurrency().name()),
            userEntity.getPhoto() == null ? "" : new String(userEntity.getPhoto()),
            null,
            new TestData(
                    passwordNew,
                    null
            )
    );
  }

}
