package stepDefinitions;

import java.io.IOException;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import base.Base;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import pageObjects.SearchBusPage;

import java.time.Duration;

/**
 * Step Definitions for the Bus Search feature.
 * Maps Gherkin steps to Java methods using Cucumber annotations.
 */
public class SearchBusStep {

    private WebDriver driver;
    private SearchBusPage searchBusPage;

    // ======================== STEP DEFINITIONS ========================

    @Given("User opens Abhibus website")
    public void user_opens_abhibus_website() {
        // Get the WebDriver instance initialized by ScenarioHook
        this.driver = hooks.ScenarioHook.driver;

        // Wait for the search form container to be present (SPA needs time to render)
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        wait.until(ExpectedConditions.presenceOfElementLocated(
                org.openqa.selenium.By.id("search-form-container")));

        String currentUrl = driver.getCurrentUrl();
        Assert.assertTrue(currentUrl.contains("abhibus"),
                "Failed to open AbhiBus website. Current URL: " + currentUrl);
        System.out.println("AbhiBus website opened successfully. URL: " + currentUrl);

        // Initialize page object after page is loaded
        searchBusPage = new SearchBusPage(driver);
    }

    @When("User enters {string} in Leaving From")
    public void user_enters_in_leaving_from(String source) throws InterruptedException {
    	Thread.sleep(2000);
        searchBusPage.enterLeavingFrom(source);
        System.out.println("Entered source city: " + source);
    }

    @When("User enters {string} in Going To")
    public void user_enters_in_going_to(String destination) throws InterruptedException {
    	Thread.sleep(2000);
        searchBusPage.enterGoingTo(destination);
        System.out.println("Entered destination city: " + destination);
    }

    @When("User selects travel date")
    public void user_selects_travel_date()throws InterruptedException {
    	Thread.sleep(2000);
        searchBusPage.selectTravelDate();
        System.out.println("Travel date selected successfully.");
    }

    @When("User clicks Search")
    public void user_clicks_search()throws InterruptedException {
    	Thread.sleep(2000);
        searchBusPage.clickSearchButton();
        System.out.println("Search button clicked.");
    }

    @Then("Bus results should appear")
    public void bus_results_should_appear()throws InterruptedException {
    	Thread.sleep(2000);
        boolean resultsDisplayed = searchBusPage.areBusResultsDisplayed();
        Assert.assertTrue(resultsDisplayed, "Bus results are not displayed on the page.");
        System.out.println("Bus results are displayed successfully.");
    }

    @When("User selects AC filter")
    public void user_selects_ac_filter()throws InterruptedException {
    	Thread.sleep(2000);
        searchBusPage.selectAcFilter();
        System.out.println("AC filter step executed.");
    }

    @When("User selects Cheapest first filter")
    public void user_selects_cheapest_first_filter()throws InterruptedException {
    	Thread.sleep(2000);
        searchBusPage.selectCheapestFirstFilter();
        System.out.println("Cheapest first filter step executed.");
    }

    @When("User selects departure time filter")
    public void user_selects_departure_time_filter()throws InterruptedException {
    	Thread.sleep(2000);
        searchBusPage.selectDepartureTimeFilter();
        System.out.println("Departure time filter step executed.");
    }
}