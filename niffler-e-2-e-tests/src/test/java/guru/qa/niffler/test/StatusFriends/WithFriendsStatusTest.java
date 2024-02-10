package guru.qa.niffler.test.StatusFriends;

import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.extension.UsersQueueExtension;
import guru.qa.niffler.model.userdata.UserJson;

import guru.qa.niffler.test.BaseWebTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static guru.qa.niffler.jupiter.annotation.User.UserType.WITH_FRIENDS;

@ExtendWith(UsersQueueExtension.class)
public class WithFriendsStatusTest extends BaseWebTest {

    @Test
    void checkHaveFriendOnAllPeople(@User(WITH_FRIENDS) UserJson user) throws Exception {
        welcomePage.open()
                .clickLogin();
        loginPage.login(user.username(), user.testData().password());
        mainPage.goToPeoplePage();
        peoplePage.checkPageUrl()
                .checkHaveFriendsDsc();
    }

    @Test
    void checkHaveFriendOnFriendsPage(@User(WITH_FRIENDS) UserJson user) throws Exception {
        welcomePage.open()
                .clickLogin();
        loginPage.login(user.username(), user.testData().password());
        mainPage.goToFriendsPage();
        friendsPage.checkPageUrl()
                .checkHaveFriendsDsc();
    }
}
