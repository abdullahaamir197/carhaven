package com.autosuite;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Home Page Tests")
class HomePageTest extends BaseTest {

    @Test
    @DisplayName("Home page loads successfully")
    void homePageLoads() {
        navigateTo("/");
        assertTrue(driver.getCurrentUrl().contains(baseUrl), "Should be on home page");
    }

    @Test
    @DisplayName("Home page has a title")
    void homePageHasTitle() {
        navigateTo("/");
        String title = driver.getTitle();
        assertNotNull(title, "Page should have a title");
    }

    @Test
    @DisplayName("Navigation bar is visible")
    void navbarIsVisible() {
        navigateTo("/");
        WebElement nav = wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("nav")));
        assertNotNull(nav, "Navigation bar should be present");
    }

    @Test
    @DisplayName("Home page contains main content")
    void homePageHasMainContent() {
        navigateTo("/");
        WebElement body = driver.findElement(By.tagName("body"));
        assertFalse(body.getText().isEmpty(), "Page should have content");
    }
}
