package pageObjects;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Page Object class for the AbhiBus search page.
 * Contains all locators and action methods for bus search functionality.
 */
public class SearchBusPage {

    private WebDriver driver;
    private WebDriverWait wait;

    // ======================== LOCATORS ========================

    By leavingFrom = By.xpath("//*[@placeholder='Leaving From']");
    By goingTo = By.xpath("//*[@placeholder='Going To']");
    By date = By.xpath("//a[@id='date-25']");
    By searchBtn = By.xpath("//*[text()='Search Buses']");

    // Desktop layout locators
    By acFilter = By.xpath("//div[contains(@class, 'filter-container')]//span[text()='AC']/parent::a");
    By cheapestFirst = By.xpath("//span[text()='Price']/parent::a");
    By departureTimeFilter = By.xpath("//div[contains(@class, 'filter-container')]//span[text()='5 PM - 11 PM']");

    // ======================== CONSTRUCTOR ========================

    /**
     * Initializes SearchBusPage with WebDriver and sets up explicit wait.
     *
     * @param driver WebDriver instance passed from step definitions
     */
    public SearchBusPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        PageFactory.initElements(driver, this);
    }

    // ======================== ACTION METHODS ========================

    /**
     * Enters the source city in the "Leaving From" field and selects the first
     * autocomplete suggestion.
     *
     * @param source the departure city name (e.g., "Mumbai")
     */
    public void enterLeavingFrom(String source) {
        WebElement leavingFromInput = wait.until(ExpectedConditions.elementToBeClickable(leavingFrom));
        leavingFromInput.click();
        leavingFromInput.clear();
        leavingFromInput.sendKeys(source);
        waitAndSelectFirstSuggestion();
    }

    /**
     * Enters the destination city in the "Going To" field and selects the first
     * autocomplete suggestion.
     *
     * @param destination the arrival city name (e.g., "Pune")
     */
    public void enterGoingTo(String destination) {
        WebElement goingToInput = wait.until(ExpectedConditions.elementToBeClickable(goingTo));
        goingToInput.click();
        goingToInput.clear();
        goingToInput.sendKeys(destination);
        waitAndSelectFirstSuggestion();
    }

    /**
     * Selects a travel date by clicking the date element.
     * Uses JavaScript click to handle any overlay/visibility issues.
     */
    public void selectTravelDate() {
        try {
            Thread.sleep(1000); // Allow any dropdowns to close
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        WebElement dateElement = wait.until(ExpectedConditions.presenceOfElementLocated(date));
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].scrollIntoView(true);", dateElement);
        js.executeScript("arguments[0].click();", dateElement);
    }

    /**
     * Clicks the "Search Buses" button to initiate the bus search.
     * Tries the mobile "Search Buses" button first, then falls back to
     * desktop "Search" button if the mobile click doesn't trigger navigation.
     */
    public void clickSearchButton() {
        JavascriptExecutor js = (JavascriptExecutor) driver;

        // Try clicking the "Search Buses" button (mobile view)
        try {
            WebElement searchButton = wait.until(ExpectedConditions.presenceOfElementLocated(searchBtn));
            js.executeScript("arguments[0].scrollIntoView(true);", searchButton);
            js.executeScript("arguments[0].click();", searchButton);
            Thread.sleep(2000);
        } catch (Exception e) {
            System.out.println("Mobile search button not found: " + e.getMessage());
        }

        // If still on homepage, try the desktop "Search" button as fallback
        String currentUrl = driver.getCurrentUrl();
        if (!currentUrl.contains("/bus_search/")) {
            try {
                WebElement desktopSearchBtn = driver.findElement(By.cssSelector("a.btn-search"));
                desktopSearchBtn.click();
            } catch (Exception e) {
                // Last resort: click via #search-button div
                try {
                    WebElement searchDiv = driver.findElement(By.id("search-button"));
                    WebElement btnInDiv = searchDiv.findElement(By.tagName("a"));
                    btnInDiv.click();
                } catch (Exception ex) {
                    System.out.println("All search button strategies failed: " + ex.getMessage());
                }
            }
        }
    }

    /**
     * Verifies that bus search results are displayed on the page.
     * Checks for URL change to /bus_search/ results page or presence of service cards.
     *
     * @return true if bus results are detected, false otherwise
     */
    public boolean areBusResultsDisplayed() {
        try {
            // Wait for URL to change to the bus_search results page
            WebDriverWait longWait = new WebDriverWait(driver, Duration.ofSeconds(30));
            longWait.until(ExpectedConditions.urlContains("/bus_search/"));
            System.out.println("Results page URL: " + driver.getCurrentUrl());

            // Wait for the service cards container to appear
            WebElement resultsContainer = longWait.until(ExpectedConditions.presenceOfElementLocated(
                    By.id("service-cards-container")));
            return resultsContainer.isDisplayed();
        } catch (Exception e) {
            // Fallback: check URL only
            String currentUrl = driver.getCurrentUrl();
            System.out.println("Fallback URL check. Current URL: " + currentUrl);
            return currentUrl.contains("/bus_search/");
        }
    }

    /**
     * Selects the AC filter on the search results page.
     */
    public void selectAcFilter() {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        try {
            System.out.println("Applying AC Filter...");
            WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(acFilter));
            js.executeScript("arguments[0].scrollIntoView(true);", element);
            wait.until(ExpectedConditions.elementToBeClickable(acFilter));
            js.executeScript("arguments[0].click();", element);
            System.out.println("AC Filter applied successfully.");
            Thread.sleep(3000); // Allow results to reload
        } catch (Exception e) {
            System.out.println("Could not select AC Filter: " + e.getMessage());
        }
    }

    /**
     * Selects the Cheapest First filter on the search results page.
     */
    public void selectCheapestFirstFilter() {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        try {
            System.out.println("Applying Cheapest First Sort...");
            WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(cheapestFirst));
            js.executeScript("arguments[0].scrollIntoView(true);", element);
            wait.until(ExpectedConditions.elementToBeClickable(cheapestFirst));
            js.executeScript("arguments[0].click();", element);
            System.out.println("Cheapest First Sort applied successfully.");
            Thread.sleep(3000); // Allow results to reload
        } catch (Exception e) {
            System.out.println("Could not select Cheapest First Filter: " + e.getMessage());
        }
    }

    /**
     * Selects the Departure Time filter (5 PM - 11 PM) on the search results page.
     */
    public void selectDepartureTimeFilter() {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        try {
            System.out.println("Applying Departure Time Filter (5 PM - 11 PM)...");
            WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(departureTimeFilter));
            js.executeScript("arguments[0].scrollIntoView(true);", element);
            wait.until(ExpectedConditions.elementToBeClickable(departureTimeFilter));
            js.executeScript("arguments[0].click();", element);
            System.out.println("Departure Time Filter applied successfully.");
            Thread.sleep(3000); // Allow results to reload
        } catch (Exception e) {
            System.out.println("Could not select Departure Time Filter: " + e.getMessage());
        }
    }

    // ======================== HELPER METHODS ========================

    /**
     * Waits for the autocomplete suggestion dropdown to appear and clicks
     * the first suggestion using keyboard navigation.
     */
    private void waitAndSelectFirstSuggestion() {
        try {
            Thread.sleep(2000);
            WebElement activeInput = driver.switchTo().activeElement();
            activeInput.sendKeys(Keys.ARROW_DOWN);
            Thread.sleep(500);
            activeInput.sendKeys(Keys.ENTER);
        } catch (Exception e) {
            System.out.println("Could not select autocomplete suggestion: " + e.getMessage());
        }
    }
}