package com.autosuite;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class BaseTest {

    protected WebDriver driver;
    protected WebDriverWait wait;
    protected static String baseUrl;

    static {
        String envBaseUrl = System.getenv("BASE_URL");
        if (envBaseUrl == null || envBaseUrl.isBlank()) {
            envBaseUrl = System.getProperty("baseUrl", "http://localhost:5173");
        }
        baseUrl = envBaseUrl.endsWith("/") ? envBaseUrl.substring(0, envBaseUrl.length() - 1) : envBaseUrl;
    }

    @BeforeEach
    void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments(
            "--headless=new",
            "--disable-gpu",
            "--no-sandbox",
            "--disable-dev-shm-usage",
            "--window-size=1920,1080"
        );

        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    protected void navigateTo(String path) {
        String normalized = path.startsWith("/") ? path : "/" + path;
        driver.get(baseUrl + normalized);
    }
}
