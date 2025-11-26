package com.autosuite;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Cars Page Tests")
class CarsPageTest extends BaseTest {

    @Test
    @DisplayName("Cars page loads successfully")
    void carsPageLoads() {
        navigateTo("/cars");
        assertTrue(driver.getCurrentUrl().contains("/cars"), "Should be on cars page");
    }

    @Test
    @DisplayName("Cars page has search input")
    void carsPageHasSearchInput() {
        navigateTo("/cars");
        WebElement searchInput = wait.until(ExpectedConditions.presenceOfElementLocated(
            By.cssSelector("input[placeholder='Search cars...']")
        ));
        assertNotNull(searchInput, "Search input should be present");
    }

    @Test
    @DisplayName("Cars page has filter controls")
    void carsPageHasFilters() {
        navigateTo("/cars");
        WebElement combobox = wait.until(ExpectedConditions.presenceOfElementLocated(
            By.cssSelector("[role='combobox']")
        ));
        assertNotNull(combobox, "Filter combobox should be present");
    }
}
