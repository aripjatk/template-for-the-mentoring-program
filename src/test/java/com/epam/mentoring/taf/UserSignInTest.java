package com.epam.mentoring.taf;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import io.restassured.http.ContentType;

public class UserSignInTest extends AbstractTest {
    private final String username = "test test";
    private final String email = "aosdijoiafejoisj@test.com";
    private final String password = "asdfg";

    private static final By LOGIN_LINK_LOCATOR = By.xpath("//li/a[@href='#/login']");
    private static final By EMAIL_INPUT_LOCATOR = By.xpath("//input[@placeholder='Email']");
    private static final By PASSWORD_INPUT_LOCATOR = By.xpath("//input[@placeholder='Password']");
    private static final By LOGIN_BUTTON_LOCATOR = By.xpath("//button[contains(text(),'Login')]");
    private static final By LOGGED_IN_USER_NAME_LOCATOR = By.xpath("//img[@class='user-pic']/parent::*");
    private static final By LOGIN_ERROR_MESSAGE_LOCATOR = By.xpath("//ul[@class='error-messages']/li");

    @Test
    public void uiVerification() {
        driver.findElement(LOGIN_LINK_LOCATOR).click();
        driver.findElement(EMAIL_INPUT_LOCATOR).sendKeys(email);
        driver.findElement(PASSWORD_INPUT_LOCATOR).sendKeys(password);
        driver.findElement(LOGIN_BUTTON_LOCATOR).click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(LOGGED_IN_USER_NAME_LOCATOR));
        String actualUserName = driver.findElement(LOGGED_IN_USER_NAME_LOCATOR).getText();
        Assert.assertEquals(actualUserName, username);
    }
    
    @Test
    public void uiNegativeVerification() {
        driver.findElement(LOGIN_LINK_LOCATOR).click();
        driver.findElement(EMAIL_INPUT_LOCATOR).sendKeys(email);
        driver.findElement(PASSWORD_INPUT_LOCATOR).sendKeys("wrong_password");
        driver.findElement(LOGIN_BUTTON_LOCATOR).click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(LOGIN_ERROR_MESSAGE_LOCATOR));
        String actualUserName = driver.findElement(LOGIN_ERROR_MESSAGE_LOCATOR).getText();
        Assert.assertEquals(actualUserName, "Wrong email/password combination");
    }
    
    @Test
    public void apiVerification() {
        given().baseUri(API_URL).when().contentType(ContentType.JSON).body(String.format("{\"user\":{\"email\":\"%s\",\"password\":\"%s\"}}", email, password)).post("/api/users/login").then().statusCode(200).body("user.email", is(email));
    }

    @Test
    public void apiNegativeVerification() {
        given().baseUri(API_URL).when().contentType(ContentType.JSON).body(String.format("{\"user\":{\"email\":\"%s\",\"password\":\"%s\"}}", email, "wrong_password")).post("/api/users/login").then().statusCode(422).body("errors.body", hasItem("Wrong email/password combination"));
    }

}
