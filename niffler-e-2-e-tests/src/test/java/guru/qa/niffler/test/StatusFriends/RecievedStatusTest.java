package guru.qa.niffler.test.StatusFriends;

import com.codeborne.selenide.Condition;
import guru.qa.niffler.jupiter.User;
import guru.qa.niffler.jupiter.UsersQueueExtension;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.test.BaseWebTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static guru.qa.niffler.jupiter.User.UserType.*;

@ExtendWith(UsersQueueExtension.class)
public class RecievedStatusTest extends BaseWebTest {

    @BeforeEach
    void doLogin(@User(RECIEVED) UserJson user) {
        loginPage.open()
                .loginInSystem(user.username(), user.testData().password());
    }

    @Test
    void checkHaveNoFriendOnAllPeopleWhenDeclined(@User(WITH_FRIENDS) UserJson userFr, @User(INVITATION_SEND) UserJson userInv) throws Exception {
        mainPage.goToPeoplePage();
        peoplePage.checkPageUrl()
                .checkNotHaveNameFriend(userFr.username());
        peoplePage.checkPageUrl()
                .checkNotHaveNameFriend(userInv.username());
    }

    @Test
    void checkNoFriendOnFriendsPage() throws Exception {
        mainPage.goToFriendsPage();
        friendsPage.checkPageUrl()
                .checkNoFriendsDsc();
    }
}
