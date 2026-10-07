package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage {
    private WebDriver driver;
    public LoginPage(WebDriver driver) {
        this.driver = driver;
    }
    private By usernameField = By.cssSelector("[data-testid='username']");
    private By passwordField = By.cssSelector("[data-testid='password']");
    private By loginButton = By.cssSelector("[data-testid='login']");

    public void login(String username, String password) {
        driver.findElement(usernameField).sendKeys(username);
        driver.findElement(passwordField).sendKeys(password);
        driver.findElement(loginButton).click();
    }
}
