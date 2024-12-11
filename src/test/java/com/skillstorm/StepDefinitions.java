package com.skillstorm;

import io.cucumber.java.AfterAll;
import io.cucumber.java.BeforeAll;
import io.cucumber.java.BeforeStep;
import io.cucumber.java.en.*;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.deque.html.axecore.results.Results;
import com.deque.html.axecore.results.Rule;
import com.deque.html.axecore.selenium.AxeBuilder;

import static org.junit.jupiter.api.Assertions.fail;

import java.time.Duration;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Assertions.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

public class StepDefinitions {
    public static WebDriver driver;
    public static Wait<WebDriver> wait;
    public static AxeBuilder axeBuilder;
    public static ExtentReports reporter;
    public static ExtentTest currentTest;
    public static Results axeResults;


    
    @BeforeAll
    public static void setup() {
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        reporter = createSparkReporter("Testing Frontend");
        // reporter = new ExtentReporter("Testing WCAG Accessiblity Compliance");]
        axeBuilder = new AxeBuilder();
    }

    @AfterAll
    public static void teardown() {
        driver.quit();
        reporter.flush();
    }

    @Given("a certain webpage {string}")
    public void aCertainWebpage(String url) {
        String shortURL = url.substring(8).trim().toUpperCase();
        currentTest = reporter.createTest("Testing WCAG Compliance: " + shortURL);
        if (waitForPageToLoad(url, true)) {
            currentTest.pass(String.format("Navigated to %s successfully", url));
        } else {
            currentTest.fail(String.format("Failed to Navigate to URL: '%s', Current URL: '%s'", 
                url, driver.getCurrentUrl()
            ));
        }
        
    }

    @When("general accessibility checks are done")
    public void generalAccessibilityChecksAreDone() {
        try {
            axeResults = axeBuilder.analyze(driver);
            currentTest.pass("Accessibility Check Complete");
        } catch (RuntimeException e) {
            currentTest.fail("Failed to Analyze Webpage: " + e.getMessage());
            fail("Failed to Analyze Webpage: " + e.getMessage());
        }
    }

    @Then("there are no WCAG violations")
    public void thereAreNoWcagViolations() {
        if (axeResults.violationFree()) {
            currentTest.pass("No Automated Accessibility Issues Found");
        } else {
            List<Rule> violations = axeResults.getViolations();
            ExtentTest infoTab = currentTest.fail("Accessibility Issues Found").createNode("Accessibility Issues Found");
            for (Rule violation: violations) {
                String msg = violation.getImpact() + violation.getId();
                //     violation.getId(),
                //     violation.getDescription(),
                //     violation.getHelp()
                // );
                infoTab.warning(msg);
            }
            fail("WCAG violations found: " + violations.size());
        } 
    }

    static boolean waitForPageToLoad(String url, boolean equals) {
        driver.get(url);
        try {
            wait.until(d -> {return driver.getCurrentUrl().equals(url) == equals;});
        } catch (Exception e) {
            System.out.println(
                String.format("Failed to Load Page:\nCurrent Page: %s\nURL Attemped: %s", 
                driver.getCurrentUrl(), url
            ));
            return false; 
        }
        return true; //This only happens if page url matches
    }

    static ExtentReports createSparkReporter(String docName) {
        ExtentReports extent = new ExtentReports();
        ExtentSparkReporter spark = new ExtentSparkReporter("target/Spark/Spark.html");
        spark.config().setTheme(Theme.DARK);
        spark.config().setDocumentTitle(docName);
        extent.attachReporter(spark);
        return extent;
    }

}
