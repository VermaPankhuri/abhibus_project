package hooks;

import base.Base;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.qameta.allure.Allure;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.ByteArrayInputStream;
import java.io.IOException;

/**
 * ScenarioHook — Global Cucumber Hooks for setup and teardown.
 *
 * These hooks run automatically before and after EVERY scenario.
 * They handle:
 *   - Browser initialization (@Before)
 *   - Screenshot on failure (@After)
 *   - Browser teardown (@After)
 *
 * Note: @Before and @After here are Cucumber hooks (io.cucumber.java),
 * NOT TestNG hooks. They wrap each Cucumber scenario.
 */
public class ScenarioHook {

    private static final Logger log = LogManager.getLogger(ScenarioHook.class);

    // Shared Base instance to manage WebDriver lifecycle
    private Base base;

    // WebDriver instance used across the scenario
    public static WebDriver driver;

    /**
     * Runs BEFORE each scenario.
     * Initializes WebDriver and navigates to the configured URL.
     *
     * @param scenario the current Cucumber Scenario (provides name/tags)
     * @throws IOException if data.properties cannot be loaded
     */
    @Before
    public void setUp(Scenario scenario) throws IOException {
       // log.info("╔══════════════════════════════════════════════════╗");
        log.info(" SCENARIO START: {}", scenario.getName());
      //  log.info("╚══════════════════════════════════════════════════╝");

        base = new Base();
        driver = base.initializeDriver();

        log.info("Browser launched successfully for scenario: {}", scenario.getName());
    }

    /**
     * Runs AFTER each scenario.
     * Captures a screenshot on failure, then quits the browser.
     *
     * @param scenario the current Cucumber Scenario (provides pass/fail status)
     */
    @After
    public void tearDown(Scenario scenario) {
      //  log.info("╔══════════════════════════════════════════════════╗");
        log.info(" SCENARIO END: {}", scenario.getName());
        log.info(" Status: {}", scenario.getStatus());
       // log.info("╚══════════════════════════════════════════════════╝");

        // Capture screenshot if scenario failed
        if (!scenario.isFailed()) {
            log.error("SCENARIO PASSED: {} — Capturing screenshot...", scenario.getName());
            try {
                byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
                scenario.attach(screenshot, "image/png", "Success Screenshot - " + scenario.getName());
                log.info("Screenshot attached to Cucumber report.");
            } catch (Exception e) {
                log.error("Failed to capture screenshot: {}", e.getMessage());
            }
        }

        // Always quit the browser after each scenario
        if (base != null) {
            base.tearDown();
            log.info("Browser closed successfully.");
        }
    }
}