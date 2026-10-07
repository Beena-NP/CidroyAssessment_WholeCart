package tests;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pages.LoginPage;

public class LoginTest extends BaseTest {

    @DataProvider(name = "loginAccounts")
    public Object[][] loginAccounts() {

        return new Object[][] {
                {"buyer1", "Catalogue · WholeCart"},
                {"buyer2", "Catalogue · WholeCart"},
                {"buyer3", "Catalogue · WholeCart"},
                {"seller1", "Orders · Seller · WholeCart"},
                {"seller2", "Orders · Seller · WholeCart"},
                {"seller3", "Orders · Seller · WholeCart"},
                {"operator", "Settings · WholeCart"}
        };
    }

    @Test(dataProvider = "loginAccounts")
    public void testValidLogin(String username, String expectedTitle) {

        LoginPage loginPage = new LoginPage(driver);

        loginPage.login(
                username,
                config.getPassword()
        );

        System.out.println("=================================");
        System.out.println("Username: " + username);
        System.out.println("Page title: " + driver.getTitle());

        Assert.assertEquals(
                driver.getTitle(),
                expectedTitle,
                "Incorrect landing page for " + username
        );
    }
}