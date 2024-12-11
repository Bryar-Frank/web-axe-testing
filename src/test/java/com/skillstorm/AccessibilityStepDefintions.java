package com.skillstorm;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.InvalidArgumentException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.deque.html.axecore.results.Results;
import com.deque.html.axecore.results.Rule;
import com.deque.html.axecore.selenium.AxeBuilder;
import com.skillstorm.utils.DriverFactory;

import io.cucumber.java.AfterAll;
import io.cucumber.java.BeforeAll;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class AccessibilityStepDefintions {
    private static WebDriver driver;
    private static WebDriverWait wait;
    private static AxeBuilder axeBuilder;
    private static Results axeResults;


    @BeforeAll
    public static void setup() {
        driver = DriverFactory.getDriver("chrome");
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // driver.manage().window().maximize();
    }

    @AfterAll
    public static void teardown() {
        if (driver != null) driver.quit();
        wait = null;
    }


    @Given("i am on webpage {string}")
    public void iAmOnWebpage(String url) {       
        driver.get(url);
        wait.until(isOnPage -> {
            return driver.getCurrentUrl().equals(url);
        });
    }

    /**
     * Performs Axe-Core Testing again specified standards 
     * @param type - the type of checks that will be performed
     *      Current options are: "Full", "WCAG Only", "Best Practices"
     *      NOTE: capitalization doesn't matter
     */
    @When("{string} accessibility checks are performed")
    public void accessibilityChecksArePerformed(String type) {

        assertTrue( configureAxeBuilder(type),
            "Failed to Configure Axe Builder"
        );
        
        axeResults = axeBuilder.analyze(driver);
    }

    @Then("there are no violations") 
    public void thereAreNoViolations() {
        if (!axeResults.violationFree()) {
            String parsedViolations = buildAxeViolationMessage();
            fail(parsedViolations);
        }
    }

    private String buildAxeViolationMessage() {
        String msg = "Problems Found: [ " + axeResults.getViolations().size() + " ]\n";

        for (Rule rule : axeResults.getViolations()) {
            // msg += "\nDescription: " + rule.getDescription().substring(0, 30);
            msg += String.format("\nID: %s | Impact: %s ", rule.getId(), rule.getImpact());
            msg += String.format("\nTags: %s", rule.getTags());
            msg += "\nRecommended: " + rule.getHelp();
        }

        return msg;
    }

    
    private boolean configureAxeBuilder(String type) {
        //Need to set this to a new AxeBuilder each time because it remembers the TAG choices from previous tests
        axeBuilder = new AxeBuilder();
        
        List<String> axeTypes = List.of("Full", "WCAG only", "Best Practices");  

        switch (type.toUpperCase()) {
            case "FULL":
                return true;
            case "WCAG ONLY":
                axeBuilder = axeBuilder.withTags(List.of(                    
                    "wcag2a",
                    "wcag2aa",
                    "wcag2aaa",
                    "wcag21a",
                    "wcag21aa",
                    "wcag22aa"
                ));
                return true;
            case "BEST PRACTICES":
                axeBuilder = axeBuilder.withTags(List.of("best-practice"));
                return true;
            default:
                throw new InvalidArgumentException( String.format(
                    "'%s' is not a valid arguement, please use one of the following: %s", type, axeTypes
                ));
        }
    }
}
