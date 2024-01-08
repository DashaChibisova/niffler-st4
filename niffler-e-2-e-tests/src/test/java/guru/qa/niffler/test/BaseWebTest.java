package guru.qa.niffler.test;

import com.codeborne.selenide.Configuration;
import guru.qa.niffler.jupiter.BrowserExtension;
import guru.qa.niffler.po.FriendsPage;
import guru.qa.niffler.po.LoginPage;
import guru.qa.niffler.po.MainPage;
import guru.qa.niffler.po.PeoplePage;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith({BrowserExtension.class})
public abstract class BaseWebTest {
    static {
        Configuration.browserSize = "1980x1024";
    }

    protected final LoginPage loginPage = new LoginPage();
    protected final MainPage mainPage = new MainPage();
    protected final FriendsPage friendsPage = new FriendsPage();
    protected final PeoplePage peoplePage = new PeoplePage();
}
