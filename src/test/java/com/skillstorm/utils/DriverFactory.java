package com.skillstorm.utils;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

public class DriverFactory {

    public static WebDriver getDriver(String type) {
               
        return switch (type.toLowerCase()) {
            case "chrome" -> getChomeDriver();
            case "firefox" -> getFoxDriver();
            default -> throw new IllegalArgumentException("Not a Browser type: " + type.toLowerCase()); 
        };
    }

    private static WebDriver getChomeDriver() {
        ChromeOptions options = getOptionsChrome();
        ChromeDriver driver =  new ChromeDriver(options);
        return driver;
    }

    private static WebDriver getFoxDriver() {
        FirefoxOptions options = new FirefoxOptions();
        // options.addArguments(getDriverOptions());
        return new FirefoxDriver(options);
    }

    private static ChromeOptions getOptionsChrome() {
        ChromeOptions chromeOptions = new ChromeOptions();

        // Get system information
        String sysHeadless = System.getProperty("headless", "false");
        Boolean headless = Boolean.parseBoolean(sysHeadless);

        // Handle headless mode.
        if(headless) {
            //Testing if we are getting to this run
            System.out.println("ADDING HEADLESS PROPERTIES");
            
            chromeOptions.addArguments( List.of(
                "--headless",
                "--no-sandbox",
                "--disable-dev-shm-usage",
                "--disable-gpu",
                "--window-size=1920,1080",
                "--remote-debugging-pipe"
            ));
        } 
        
        // Create and return option list.
        return chromeOptions;
    }
}
