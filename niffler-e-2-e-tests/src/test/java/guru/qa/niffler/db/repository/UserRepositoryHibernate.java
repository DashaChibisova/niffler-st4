package guru.qa.niffler.db.repository;

import guru.qa.niffler.db.EmfProvider;
import guru.qa.niffler.db.jpa.JpaService;
import guru.qa.niffler.db.model.FriendsEntity;
import guru.qa.niffler.db.model.UserAuthEntity;
import guru.qa.niffler.db.model.UserEntity;
import guru.qa.niffler.model.userdata.FriendJson;
import guru.qa.niffler.model.userdata.FriendState;
import guru.qa.niffler.model.userdata.UserJson;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static guru.qa.niffler.db.Database.AUTH;
import static guru.qa.niffler.db.Database.USERDATA;

public class UserRepositoryHibernate extends JpaService implements UserRepository {

  private final PasswordEncoder pe = PasswordEncoderFactories.createDelegatingPasswordEncoder();

  public UserRepositoryHibernate() {
    super(
        Map.of(
            AUTH, EmfProvider.INSTANCE.emf(AUTH).createEntityManager(),
            USERDATA, EmfProvider.INSTANCE.emf(USERDATA).createEntityManager()
        )
    );
  }

  @Override
  public UserAuthEntity createInAuth(UserAuthEntity user) {
    String originalPassword = user.getPassword();
    user.setPassword(pe.encode(originalPassword));
    persist(AUTH, user);
    return user;
  }

  @Override
  public Optional<UserAuthEntity> findByIdInAuth(UUID id) {
    return Optional.of(entityManager(AUTH).find(UserAuthEntity.class, id));
  }

  @Override
  public UserEntity createInUserdata(UserEntity user) {
    persist(USERDATA, user);
    return user;
  }

  @Override
  public Optional<UserEntity> findByIdInUserdata(UUID id) {
    return Optional.of(entityManager(USERDATA).find(UserEntity.class, id));
  }

  @Override
  public void deleteInAuthById(UUID id) {
    UserAuthEntity toBeDeleted = findByIdInAuth(id).get();
    remove(AUTH, toBeDeleted);
  }

  @Override
  public void deleteInUserdataById(UUID id) {
    UserEntity toBeDeleted = Optional.of(entityManager(USERDATA).find(UserEntity.class, id)).get();
    remove(USERDATA, toBeDeleted);
  }

  @Override
  public UserEntity updateInUserDataById(UserEntity user) {
    Optional<UserEntity> currentUser = findByIdInUserdata(user.getId());
    UserEntity currentEntity = currentUser.get();
    persist(USERDATA, currentEntity);

    return null;
  }

  @Override
  public UserAuthEntity updateInAuthById(UserAuthEntity userAuth) {
    return null;
  }

  @Override
  public UserEntity findByName(String name) {
    return entityManager(USERDATA).find(UserEntity.class, name);
  }

  @Override
  public UserEntity addFriends(UUID username, UUID friend) {
    Optional<UserEntity> currentUser = findByIdInUserdata(username);
    Optional<UserEntity> friendUser = findByIdInUserdata(friend);
    UserEntity currentEntity = currentUser.get();
    UserEntity friendEntity = friendUser.get();

    currentEntity.addFriends(false, friendEntity);
    friendEntity.addFriends(false, currentEntity);
    persist(USERDATA, currentEntity);
    persist(USERDATA, friendEntity);
    return currentEntity;
  }

  @Override
  public UserEntity incomeInvitation(UUID incomeInvitation, UUID userFriend) {
    Optional<UserEntity> incomeInvitationUser = findByIdInUserdata(incomeInvitation);
    Optional<UserEntity> friendUser = findByIdInUserdata(userFriend);
    UserEntity incomeInvitationUserEntity = incomeInvitationUser.get();
    UserEntity friendEntity = friendUser.get();

    friendEntity.addFriends(true, incomeInvitationUserEntity);
    persist(USERDATA, friendEntity);
    return incomeInvitationUserEntity;
  }

  @Override
  public UserEntity outcomeInvitation(UUID userFriend, UUID outcomeInvitation) {
    Optional<UserEntity> outcomeInvitationUser = findByIdInUserdata(outcomeInvitation);
    Optional<UserEntity> friendUser = findByIdInUserdata(userFriend);
    UserEntity outcomeInvitationUserEntity = outcomeInvitationUser.get();
    UserEntity friendUserEntity = friendUser.get();

    outcomeInvitationUserEntity.addFriends(true, friendUserEntity);
    persist(USERDATA, outcomeInvitationUser);
    return outcomeInvitationUserEntity;
  }

}
