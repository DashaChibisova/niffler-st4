package guru.qa.niffler.test.StatusFriends;

import guru.qa.niffler.jupiter.User;
import guru.qa.niffler.jupiter.UsersQueueExtension;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.test.BaseWebTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static guru.qa.niffler.jupiter.User.UserType.INVITATION_SEND;

@ExtendWith(UsersQueueExtension.class)
public class InvitationStatusTest extends BaseWebTest {

    @BeforeEach
    void doLogin(@User(INVITATION_SEND) UserJson user) {
        loginPage.open()
                .loginInSystem(user.username(), user.testData().password());
    }

    @Test
    void checkHavePendingInvitationOnAllPeople() throws Exception {
        mainPage.goToPeoplePage();
        peoplePage.checkPageUrl()
                .checkHavePendingInvitation();
    }

    @Test
    void checkHaveNoFriendOnFriendsPageWhenPendingInvitation() throws Exception {
        mainPage.goToFriendsPage();
        friendsPage.checkPageUrl()
                .checkNoFriendsDsc();
    }
}
