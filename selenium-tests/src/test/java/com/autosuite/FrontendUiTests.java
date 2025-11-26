package com.autosuite;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.github.bonigarcia.wdm.WebDriverManager;

class FrontendUiTests {

  private static WebDriver driver;
  private static WebDriverWait wait;
  private static String baseUrl;

  @BeforeAll
  static void setUpSuite() {
    String envBaseUrl = System.getenv("BASE_URL");
    if (envBaseUrl == null || envBaseUrl.isBlank()) {
      envBaseUrl = System.getProperty("baseUrl", "http://localhost:5173");
    }
    baseUrl = envBaseUrl.endsWith("/") ? envBaseUrl.substring(0, envBaseUrl.length() - 1) : envBaseUrl;

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
    driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(2));
    wait = new WebDriverWait(driver, Duration.ofSeconds(12));
  }

  @AfterAll
  static void tearDownSuite() {
    if (driver != null) {
      driver.quit();
    }
  }

  private static void goTo(String path) {
    String normalized = path.startsWith("/") ? path : "/" + path;
    driver.get(baseUrl + normalized);
  }

  @Test
  @DisplayName("Home hero section displays the primary heading")
  void homeHeroHeadingIsVisible() {
    goTo("/");
    WebElement heading = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("section h1")));
    assertTrue(heading.getText().contains("Find Your Dream Car"), "Hero heading should mention 'Find Your Dream Car'");
  }

  @Test
  @DisplayName("Navbar shows the CarHaven brand text")
  void navbarBrandDisplaysCarHaven() {
    goTo("/");
    WebElement brand = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("nav a[href='/'] span")));
    assertEquals("CarHaven", brand.getText(), "Navbar brand text should be CarHaven");
  }

  @Test
  @DisplayName("Hero section renders the Browse Cars button")
  void homeHeroHasBrowseCarsButton() {
    goTo("/");
    WebElement browseButton = wait.until(ExpectedConditions.visibilityOfElementLocated(
        By.xpath("//a[@href='/cars']/button[contains(., 'Browse Cars')]")
    ));
    assertTrue(browseButton.isDisplayed(), "Browse Cars button should be visible");
  }

  @Test
  @DisplayName("CTA card shows the secondary Get Started Now button")
  void homeCtaSectionHasSecondaryButton() {
    goTo("/");
    WebElement ctaButton = wait.until(ExpectedConditions.visibilityOfElementLocated(
        By.xpath("//section[contains(@class,'py-20')]//button[contains(., 'Get Started Now')]")
    ));
    assertTrue(ctaButton.isDisplayed(), "CTA Get Started Now button should be visible");
  }

  @Test
  @DisplayName("Clicking Browse Cars navigates to the cars listing page")
  void browseCarsButtonNavigatesToCarsPage() {
    goTo("/");
    WebElement browseButton = wait.until(ExpectedConditions.elementToBeClickable(
        By.xpath("//a[@href='/cars']/button[contains(., 'Browse Cars')]")
    ));
    browseButton.click();
    wait.until(ExpectedConditions.urlContains("/cars"));
    WebElement heading = wait.until(ExpectedConditions.visibilityOfElementLocated(
        By.xpath("//h1[contains(., 'Browse Our Collection')]")
    ));
    assertEquals("Browse Our Collection", heading.getText());
  }

  @Test
  @DisplayName("Cars page renders the search input")
  void carsPageContainsSearchInput() {
    goTo("/cars");
    WebElement searchInput = wait.until(ExpectedConditions.visibilityOfElementLocated(
        By.cssSelector("input[placeholder='Search cars...']")
    ));
    assertTrue(searchInput.isDisplayed(), "Search input should be visible on cars page");
  }

  @Test
  @DisplayName("Cars page shows both brand and sort comboboxes")
  void carsPageRendersFilterComboboxes() {
    goTo("/cars");
    ExpectedCondition<Boolean> twoComboboxesVisible = drv -> {
      List<WebElement> comboboxes = drv.findElements(By.cssSelector("[role='combobox']"));
      return comboboxes.size() >= 2 && comboboxes.stream().allMatch(WebElement::isDisplayed);
    };
    assertTrue(wait.until(twoComboboxesVisible), "Two combobox filters should be visible on cars page");
  }

  @Test
  @DisplayName("Cars page eventually shows inventory cards or fallback messaging")
  void carsPageShowsInventoryOrFallback() {
    goTo("/cars");
    ExpectedCondition<Boolean> inventoryOrFallback = drv -> {
      List<WebElement> cards = drv.findElements(By.cssSelector("a[href^='/cars/']"));
      List<WebElement> fallbackHeading = drv.findElements(By.xpath("//*[contains(text(),'No cars found')]"));
      List<WebElement> loadingText = drv.findElements(By.xpath("//*[contains(text(),'Loading cars...')]") );
      return !cards.isEmpty() || !fallbackHeading.isEmpty() || !loadingText.isEmpty();
    };
    assertTrue(wait.until(inventoryOrFallback), "Cars page should present inventory, fallback, or loading state");
  }

  @Test
  @DisplayName("Visiting an unknown route renders the 404 page")
  void unknownRouteRendersNotFound() {
    goTo("/does-not-exist");
    WebElement statusCode = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h1[text()='404']")));
    assertEquals("404", statusCode.getText(), "Unknown route should show 404 page");
  }

  @Test
  @DisplayName("Auth page reveals the name field when switching to sign-up")
  void authSignUpFlowShowsNameField() {
    goTo("/auth");
    WebElement toggleButton = wait.until(ExpectedConditions.elementToBeClickable(
        By.xpath("//button[contains(., 'Need an account? Sign up')]")
    ));
    toggleButton.click();

    WebElement createHeading = wait.until(ExpectedConditions.visibilityOfElementLocated(
        By.xpath("//h2[contains(., 'Create Account')]")
    ));
    assertEquals("Create Account", createHeading.getText(), "Sign-up view heading should be Create Account");

    WebElement nameInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#name")));
    assertTrue(nameInput.isDisplayed(), "Name input should be visible in sign-up mode");
  }
}
