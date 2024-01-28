package guru.qa.niffler.db.repository;

import guru.qa.niffler.db.model.CurrencyValues;
import guru.qa.niffler.db.model.UserAuthEntity;
import guru.qa.niffler.db.model.UserEntity;

import java.util.UUID;

public interface UserRepository {

    UserAuthEntity createInAuth(UserAuthEntity user);

    UserEntity createInUserdata(UserEntity user);

    void deleteInAuthById(UUID id);

    void deleteInUserDataById(UUID id);

    void updateInUserDataById(UUID idUser, String userName, CurrencyValues currency);

    void updateInAuthById(UserAuthEntity userAuth);

    UserEntity getUserDataByName(String userName);

    UserAuthEntity getUserAuthByName(String userName);
}
