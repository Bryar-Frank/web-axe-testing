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
        ChromeOptions options = new ChromeOptions();
        options.addArguments(getDriverOptions());
        return new ChromeDriver(options);
    }

    private static WebDriver getFoxDriver() {
        FirefoxOptions options = new FirefoxOptions();
        options.addArguments(getDriverOptions());
        return new FirefoxDriver(options);
    }

    private static List<String> getDriverOptions() {
        List<String> driverOptions;

        // Get system information
        String sysHeadless = System.getProperty("headless", "false");
        Boolean headless = Boolean.parseBoolean(sysHeadless);

        // Handle headless mode.
        // if(headless) {
        if(true) {
            //Testing if we are getting to this run
            System.out.println("ADDING HEADLESS PROPERTIES");
            driverOptions = List.of(
                "--headless",
                "--no-sandbox",
                "--disable-dev-shm-usage",
                "--disable-gpu",
                "--window-size=1920,1080",
                "--remote-debugging-pipe"
            );
        } 
        // else {
        //     driverOptions = new ArrayList<>();
        // }
        
        // Create and return option list.
        return driverOptions;
    }
}
