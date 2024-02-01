package guru.qa.niffler.db.repository;

import guru.qa.niffler.db.model.CurrencyValues;
import guru.qa.niffler.db.model.UserAuthEntity;
import guru.qa.niffler.db.model.UserEntity;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository {

    UserAuthEntity createInAuth(UserAuthEntity user);

    UserEntity createInUserdata(UserEntity user);

    Optional<UserAuthEntity> findByIdInAuth(UUID id);

    Optional<UserEntity> findByIdInUserdata(UUID id);

    void deleteInAuthById(UUID id);

    void deleteInUserdataById(UUID id);

    UserEntity updateInUserDataById(UserEntity user);

    UserAuthEntity updateInAuthById(UserAuthEntity userAuth);
}
