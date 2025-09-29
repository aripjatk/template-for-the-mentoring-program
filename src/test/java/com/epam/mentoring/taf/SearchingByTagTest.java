package com.epam.mentoring.taf;

import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class SearchingByTagTest extends AbstractTest {
    private static final By TAG_PILL_LOCATOR = By.xpath("//button[contains(@class,'tag-pill')]");
    private static final By ACTIVE_TAG_LOCATOR = By.xpath("//button[@class='nav-link active']");
    private static final By ARTICLE_PREVIEW_LOCATOR = By.xpath("//div[@class='app-article-preview' and not(@hidden)]");

    private static final String GET_ARTICLES_BY_TAG_API_ENDPOINT = "/api/articles?tag={tag}&limit=10&offset=0";

    @Test
    public void uiVerification() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(TAG_PILL_LOCATOR));

        List<WebElement> tags = driver.findElements(TAG_PILL_LOCATOR);
        int randomTagNumber = (int) (Math.random() * tags.size());
        WebElement tag = tags.get(randomTagNumber);
        String tagName = tag.getText();
        tag.click();

        wait.until(ExpectedConditions.invisibilityOfElementLocated(ARTICLE_PREVIEW_LOCATOR));
        String selectedTag = driver.findElement(ACTIVE_TAG_LOCATOR).getText();
        Assert.assertEquals(selectedTag, tagName);
    }

    @Test
    public void apiVerification1() {
        String tag = "test";
        given().baseUri(API_URL).when().get(GET_ARTICLES_BY_TAG_API_ENDPOINT, tag).then().statusCode(200).body("articles[1].tagList", hasItem("test"));
    }

    @Test
    public void apiVerification2() {
        String tag = "backend";
        given().baseUri(API_URL).when().get(GET_ARTICLES_BY_TAG_API_ENDPOINT, tag).then().statusCode(200).body("articles[1].tagList", hasItem("backend"));
    }

    @Test
    public void apiVerification3() {
        String tag = "frontend";
        given().baseUri(API_URL).when().get(GET_ARTICLES_BY_TAG_API_ENDPOINT, tag).then().statusCode(200).body("articles[1].tagList", hasItem("frontend"));
    }

    @Test
    public void apiNegativeVerification() {
        String tag = "invalid_tag_name";
        given().baseUri(API_URL).when().get(GET_ARTICLES_BY_TAG_API_ENDPOINT, tag).then().statusCode(200).body("articles.size()", equalTo(0));
    }

}
