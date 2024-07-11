package ru.netology.driver;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class ChromeDriverSingleton {
    private static ChromeDriverSingleton instance = null;
    private WebDriver chromeDriver;

    public ChromeDriverSingleton() {
        System.setProperty("webdriver.chrome.driver", System.getenv("CHROME_DRIVER"));
        chromeDriver = new ChromeDriver();
    }

    public static ChromeDriverSingleton getInstance() {
        if (instance == null) {
            instance = new ChromeDriverSingleton();
        }
        return instance;
    }

    public WebDriver getDriver() {
        return chromeDriver;
    }

    public void quitDriver() {
        if (chromeDriver != null) {
            chromeDriver.quit();
            chromeDriver = null;
            instance = null;
        }
    }
}
