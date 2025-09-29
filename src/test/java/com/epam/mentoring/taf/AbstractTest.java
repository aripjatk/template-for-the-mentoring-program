package com.epam.mentoring.taf;

import java.net.MalformedURLException;
import java.net.URL;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

import io.github.bonigarcia.wdm.WebDriverManager;

abstract public class AbstractTest {

    protected final static String baseUrl = "https://conduit-realworld-example-app.fly.dev/";
    protected final static String UI_URL = "https://conduit-realworld-example-app.fly.dev/";
    protected final static String API_URL = "https://conduit-realworld-example-app.fly.dev";
    protected WebDriver driver;
    protected WebDriverWait wait;

    @BeforeClass
    public void initialisation() throws MalformedURLException {
        String gridUrl = System.getProperty("grid.url");
        if (gridUrl == null) {
            WebDriverManager.chromedriver().setup();
            driver = new ChromeDriver();
        } else {
            driver = new RemoteWebDriver(new URL(gridUrl), DesiredCapabilities.chrome());
        }
        driver.get(baseUrl);
        wait = new WebDriverWait(driver, 2);
    }

    @AfterClass
    public void terminate() {
        driver.quit();
    }

}
