package guru.qa.niffler.test;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.db.model.*;
import guru.qa.niffler.jupiter.DbUserExtension;
import guru.qa.niffler.jupiter.annotation.DbUser;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

@ExtendWith(DbUserExtension.class)
public class Login2Test extends BaseWebTest {

  @DbUser(username = "3333", password = "3333")
  @Test
  void statisticShouldBeVisibleAfterLogin(UserAuthEntity userAuth) {
    Selenide.open("http://127.0.0.1:3000/main");
    $("a[href*='redirect']").click();
    $("input[name='username']").setValue(userAuth.getUsername());
    $("input[name='password']").setValue(userAuth.getPassword());
    $("button[type='submit']").click();
    $(".main-content__section-stats").should(visible);
  }
}
