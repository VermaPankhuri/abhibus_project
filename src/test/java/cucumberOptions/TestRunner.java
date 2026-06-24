package cucumberOptions;

import org.testng.annotations.DataProvider;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

/**
 * TestNG runner class for Cucumber.
 * Configures feature file location, step definitions glue, and report plugins.
 */
@CucumberOptions(
        features = "src/test/resources/features",
        glue = {"stepDefinitions", "hooks"},
        plugin = {
                "pretty",
                //"html:target/cucumber-reports/cucumber.html",
                //"json:target/cucumber-reports/cucumber.json",
                "io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm"
        },
        monochrome = true
)
public class TestRunner extends AbstractTestNGCucumberTests {
	  @DataProvider(parallel=true)
	  public Object[][] scenarios(){
		  return
	  super.scenarios(); }
}