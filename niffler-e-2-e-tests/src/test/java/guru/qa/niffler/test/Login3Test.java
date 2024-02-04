package guru.qa.niffler.test;

import guru.qa.niffler.db.model.*;
import guru.qa.niffler.db.repository.UserRepository;
import guru.qa.niffler.jupiter.UserRepositoryExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Arrays;
import java.util.UUID;

@ExtendWith(UserRepositoryExtension.class)
public class Login3Test extends BaseWebTest {

  private UserRepository userRepository;

//  private UserAuthEntity userAuth;
//  private UserEntity user;


//  @BeforeEach
//  void createUser() {
//    userAuth = new UserAuthEntity();
//    userAuth.setId(UUID.fromString("79b537ce-bdef-11ee-b188-0242ac110002"));
//    userAuth.setUsername("valentin_4");
//    userAuth.setPassword("12345");
//    userAuth.setEnabled(true);
//    userAuth.setAccountNonExpired(true);
//    userAuth.setAccountNonLocked(true);
//    userAuth.setCredentialsNonExpired(true);
//    userAuth.setAuthorities(Arrays.stream(Authority.values())
//            .map(e -> {
//              AuthorityEntity ae = new AuthorityEntity();
//              ae.setAuthority(e);
//              return ae;
//            }).toList()
//    );
//
//    user = new UserEntity();
//    user.setUsername("valentin_4");
//    user.setCurrency(CurrencyValues.RUB);
//    userRepository.createInAuth(userAuth);
//    userRepository.createInUserdata(user);
//  }

//  @AfterEach
//  void removeUser() {
//    userRepository.deleteInAuthById(userAuth.getId());
//    userRepository.deleteInUserdataById(user.getId());
//  }

  @Test
  void checkFunkUpdateDataUserAuth() {
    UserAuthEntity userAuth = new UserAuthEntity();
    userAuth.setId(UUID.fromString("79b537ce-bdef-11ee-b188-0242ac110002"));
    userAuth.setUsername("valentin_80");
    userAuth.setPassword("12345");
    userAuth.setEnabled(true);
    userAuth.setAccountNonExpired(true);
    userAuth.setAccountNonLocked(true);
    userAuth.setCredentialsNonExpired(true);
    userAuth.setAuthorities(Arrays.stream(Authority.values())
            .map(e -> {
              AuthorityEntity ae = new AuthorityEntity();
              ae.setAuthority(e);
              return ae;
            }).toList()
    );

    userRepository.updateInAuthById(userAuth);
  }

  @Test
  void checkFunkUpdateDataUserUserEntity() {
    UserEntity user2 = new UserEntity();
    user2.setId(UUID.fromString("79c0cc42-bdef-11ee-90a3-0242ac110002"));
    user2.setUsername("valentin_153");
    user2.setCurrency(CurrencyValues.RUB);
    userRepository.updateInUserDataById(user2);
  }
}
