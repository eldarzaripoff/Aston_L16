package ru.netology.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import ru.netology.helpers.XPaths;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

public class MTSPageWithFrame {
    private WebDriver chromeDriver;
    private WebDriverWait wait;
    private List<WebElement> icons;

    public MTSPageWithFrame(WebDriver chromeDriver) {
        this.chromeDriver = chromeDriver;
        wait = new WebDriverWait(chromeDriver, Duration.ofSeconds(10));
    }

    public String stringModifier(String xPath, String regex, String replacement) {
        WebElement webElement = chromeDriver.findElement(By.xpath(xPath));
        return webElement.getText().replaceAll(regex, replacement);
    }

    public WebElement getPlaceHolder(String xPath) {
        return chromeDriver.findElement(By.xpath(xPath));
    }

    public List<String> getIconSourcesAsStrings() {
        icons = chromeDriver.findElements(By.xpath(XPaths.ICONS_XPATH));
        return icons.stream()
                .map(element -> element.getAttribute("src"))
                .collect(Collectors.toList());
    }
}
