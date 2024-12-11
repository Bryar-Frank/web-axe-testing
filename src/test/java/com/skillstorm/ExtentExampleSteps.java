package com.skillstorm;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;

import com.skillstorm.utils.MyExtentReporter;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class ExtentExampleSteps {
    private static MyExtentReporter reporter;

    @Given("i have an extent example")
    public void iHaveAnExtentExample() {
        reporter = new MyExtentReporter();
    }

    @When("i run the example")
    public void iRunTheExample() {
        reporter.runExample();
    }

    @Then("an example report is created") 
    public void anExampleReportIsCreated() {
        assertTrue(new File(MyExtentReporter.getPathtoReport()).isFile());
    }
}
