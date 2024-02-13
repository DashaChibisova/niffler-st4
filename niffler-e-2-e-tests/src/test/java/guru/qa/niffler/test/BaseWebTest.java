package guru.qa.niffler.test;

import com.codeborne.selenide.Configuration;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.extension.BrowserExtension;
import guru.qa.niffler.page.*;
import guru.qa.niffler.page.PeoplePage;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith({BrowserExtension.class})
public abstract class BaseWebTest {
    static {
        Configuration.browserSize = "1980x1024";
    }

    protected static final Config CFG = Config.getInstance();

    protected final RegisterPage registerPage = new RegisterPage();
    protected final WelcomePage welcomePage = new WelcomePage();
    protected final LoginPage loginPage = new LoginPage();
    protected final MainPage mainPage = new MainPage();
    protected final FriendsPage friendsPage = new FriendsPage();
    protected final PeoplePage peoplePage = new PeoplePage();
}
