package tests;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.LoginPage;

public class LoginTest extends BaseTest {
    @Test
    public void testValidBuyerLogin()
    {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login(config.getUsername(), config.getPassword());
        System.out.println("The page title is : "+driver.getTitle());
        Assert.assertEquals(driver.getTitle(),"Catalogue · WholeCart");
    }
}
