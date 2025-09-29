package com.epam.mentoring.taf;

import static org.hamcrest.Matchers.hasItem;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import io.restassured.http.ContentType;

public class UserSignUpTest extends AbstractTest {
    private final String username = "Ari Test User";
    private final String email = "ari_test_user@example.com";
    private final String password = "test_password";

    private static final By SIGN_UP_LINK_LOCATOR = By.xpath("//li/a[@href='#/register']");
    private static final By USER_NAME_INPUT_LOCATOR = By.xpath("//input[@placeholder='Your Name']");
    private static final By EMAIL_INPUT_LOCATOR = By.xpath("//input[@placeholder='Email']");
    private static final By PASSWORD_INPUT_LOCATOR = By.xpath("//input[@placeholder='Password']");
    private static final By SIGN_UP_BUTTON_LOCATOR = By.xpath("//button[contains(text(), 'Sign up')]");
    private static final By LOGGED_IN_USER_NAME_LOCATOR = By.xpath("//img[@class='user-pic']/parent::*");
    
    @Test
    public void uiVerification() {
        int uniqueId = (int) (Math.random() * 100);
        String finalUsername = this.username + uniqueId;
        String finalEmail = this.email.replace("@", "." + uniqueId + "@");

        driver.get(UI_URL);
        driver.findElement(SIGN_UP_LINK_LOCATOR).click();
        driver.findElement(USER_NAME_INPUT_LOCATOR).sendKeys(finalUsername);
        driver.findElement(EMAIL_INPUT_LOCATOR).sendKeys(finalEmail);
        driver.findElement(PASSWORD_INPUT_LOCATOR).sendKeys(password);
        driver.findElement(SIGN_UP_BUTTON_LOCATOR).click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(LOGGED_IN_USER_NAME_LOCATOR));

        String actualUserName = driver.findElement(LOGGED_IN_USER_NAME_LOCATOR).getText();
        Assert.assertEquals(actualUserName, finalUsername);
    }

    @Test
    public void apiVerification() {
        int uniqueId = (int) (Math.random() * 100);
        String finalUserName = this.username + uniqueId;
        String finalEmail = this.email.replace("@", "." + uniqueId + "@");

        given().baseUri(API_URL).when().contentType(ContentType.JSON).body(String.format("{\"user\":{\"email\":\"%s\",\"password\":\"%s\",\"username\":\"%s\"}}", finalEmail, password, finalUserName)).post("/api/users").then().statusCode(201);
    }

    @Test
    public void apiAlreadyRegisteredVerification() {
        given().baseUri(API_URL).when().contentType(ContentType.JSON).body(String.format("{\"user\":{\"email\":\"%s\",\"password\":\"%s\",\"username\":\"%s\"}}", email, password, username)).post("/api/users").then().statusCode(422).body("errors.body", hasItem("Email already exists.. try logging in"));
    }

    /*
    * The API does not check the validity of the email address
    * @Test
    * public void apiWrongEmailVerification() {
    *     given().baseUri(API_URL).when().contentType(ContentType.JSON).body(String.format("{\"user\":{\"email\":\"%s\",\"password\":\"%s\",\"username\":\"%s\"}}", "wrong_email", password, username)).post("/api/users").then().statusCode(422).body("errors.email", hasItem("is invalid"));
    * }
    */

}
