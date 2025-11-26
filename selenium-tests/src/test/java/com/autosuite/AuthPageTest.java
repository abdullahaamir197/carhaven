package com.autosuite;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Auth Page Tests")
class AuthPageTest extends BaseTest {

    @Test
    @DisplayName("Auth page loads successfully")
    void authPageLoads() {
        navigateTo("/auth");
        assertTrue(driver.getCurrentUrl().contains("/auth"), "Should be on auth page");
    }

    @Test
    @DisplayName("Auth page has email input")
    void authPageHasEmailInput() {
        navigateTo("/auth");
        WebElement emailInput = wait.until(ExpectedConditions.presenceOfElementLocated(
            By.cssSelector("input[type='email']")
        ));
        assertNotNull(emailInput, "Email input should be present");
    }

    @Test
    @DisplayName("Auth page has password input")
    void authPageHasPasswordInput() {
        navigateTo("/auth");
        WebElement passwordInput = wait.until(ExpectedConditions.presenceOfElementLocated(
            By.cssSelector("input[type='password']")
        ));
        assertNotNull(passwordInput, "Password input should be present");
    }
}
