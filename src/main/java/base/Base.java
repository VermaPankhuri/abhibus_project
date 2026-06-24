package base;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.Properties;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.github.bonigarcia.wdm.WebDriverManager;

/**
 * Base class that initializes the WebDriver and loads configuration properties.
 * All Page Object classes extend this to access the shared driver instance.
 */
public class Base {

    public WebDriver driver;
    public WebDriverWait wait;
    public Properties properties;

    /**
     * Loads configuration from data.properties file.
     *
     * @return Properties object containing configuration key-value pairs
     * @throws IOException if the properties file cannot be read
     */
    public Properties loadProperties() throws IOException {
        properties = new Properties();
        FileInputStream fis = new FileInputStream(
                System.getProperty("user.dir") + "/src/main/resources/data.properties");
        properties.load(fis);
        fis.close();
        return properties;
    }

    /**
     * Initializes the WebDriver based on the browser specified in data.properties.
     * Sets up implicit wait, maximizes the window, and navigates to the configured URL.
     *
     * @return initialized WebDriver instance
     * @throws IOException if properties file cannot be loaded
     */
    public WebDriver initializeDriver() throws IOException {
        properties = loadProperties();
        String browserName = properties.getProperty("browser");

        if (browserName.equalsIgnoreCase("chrome")) {
            WebDriverManager.chromedriver().setup();
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--disable-notifications");
            options.addArguments("--start-maximized");
            driver = new ChromeDriver(options);
        } else {
            throw new IllegalArgumentException("Browser \"" + browserName + "\" is not supported. Use 'chrome'.");
        }

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        driver.get(properties.getProperty("url"));

        return driver;
    }

    /**
     * Quits the WebDriver and cleans up the browser session.
     */
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}