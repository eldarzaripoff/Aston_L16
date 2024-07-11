package by.mts;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import ru.netology.driver.ChromeDriverSingleton;

import java.util.concurrent.TimeUnit;

public class BaseTests {
    protected WebDriver chromeDriver;

    @BeforeEach
    public void setChromeDriver() {
        chromeDriver = ChromeDriverSingleton.getInstance().getDriver();
        chromeDriver.manage().window().maximize();
        chromeDriver.manage().timeouts().pageLoadTimeout(45, TimeUnit.SECONDS);

    }

    @AfterEach
    public void after() {
        ChromeDriverSingleton.getInstance().quitDriver();
    }
}
