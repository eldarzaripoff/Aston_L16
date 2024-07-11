package ru.netology.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import ru.netology.driver.ChromeDriverSingleton;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MTSMainPage {

    private final String IFRAME_XPATH = "//iframe[@class='bepaid-iframe']";

    private final String LOGO_LIST_XPATH = "//img[contains(@src, " +
            "'/local/templates/new_design/assets/html/images/pages/index/pay')]";
    private WebDriver chromeDriver;
    private WebDriverWait wait;
    private List<WebElement> logoList;

    public MTSMainPage() {
        this.chromeDriver = ChromeDriverSingleton.getInstance().getDriver();
        wait = new WebDriverWait(chromeDriver, Duration.ofSeconds(20));
    }

    public List<String> getIconSourcesAsStrings() {
        String iconsXpath = "//div[@class='cards-brands cards-brands__container ng-tns-c61-0 ng-trigger ng-trigger-brandsState ng-star-inserted']//img";
        List<WebElement> icons = chromeDriver.findElements(By.xpath(iconsXpath));
        return icons.stream()
                .map(element -> element.getAttribute("src"))
                .collect(Collectors.toList());
    }

    public WebElement getButtonForSelectOptionInList(String option) {
        WebElement buttonForSelectOptionInList = chromeDriver.findElement(By.xpath("//p[contains(text(), '" + option + "')]"));
        wait.until(ExpectedConditions.elementToBeClickable(buttonForSelectOptionInList));
        return buttonForSelectOptionInList;
    }

    public WebElement getListUnrollButton() {
        String buttonForListUnrollXpath = "//button[@class='select__header']";
        return chromeDriver.findElement(By.xpath(buttonForListUnrollXpath));
    }

    public WebElement getCookieButton() {
        String cookieButtonXPath = "//button[@class='btn btn_black cookie__ok']";
        WebElement cookieButton = chromeDriver.findElement(By.xpath(cookieButtonXPath));
        wait.until(ExpectedConditions.elementToBeClickable(cookieButton));
        return cookieButton;
    }

    public List<WebElement> getLogoList() {
        logoList = chromeDriver.findElements(By.xpath(LOGO_LIST_XPATH));
        return logoList;
    }

    public WebElement getButtonInDetails() {
        String buttonInDetailsXpath = "//a[contains(text(), 'Подробнее о сервисе')]";
        WebElement buttonInDetails = chromeDriver.findElement(By.xpath(buttonInDetailsXpath));
        wait.until(ExpectedConditions.elementToBeClickable(buttonInDetails));
        return buttonInDetails;
    }

    public WebElement getTitle() {
        String titleOfFormXpath = "//h2[contains(text(), 'Онлайн пополнение')]";
        return chromeDriver.findElement(By.xpath(titleOfFormXpath));
    }

    public WebElement getPlaceHolder(String xPath) {
        return chromeDriver.findElement(By.xpath(xPath));
    }

    public List<String> getLogoListAsListOfStrings() {
        logoList = chromeDriver.findElements(By.xpath(LOGO_LIST_XPATH));
        return logoList.stream()
                .map(webElement -> webElement.getAttribute("alt"))
                .collect(Collectors.toList());
    }

    public String getCurrentUrl() {
        return chromeDriver.getCurrentUrl();
    }

    /*
    Метод для заполнения анкеты в блоке "Услуги связи"
     */
    public void fillCommunicationServices(Map<String, String> inputData) {
        WebElement phoneNumber = chromeDriver.findElement(By.xpath("//div[@class='input-wrapper input-wrapper_label-left']" +
                "/input[@class='phone'][../label[@for='connection-phone']]"));
        phoneNumber.sendKeys(inputData.get("phone"));

        WebElement sum = chromeDriver.findElement(By.xpath("//div[@class='input-wrapper input-wrapper_label-right']" +
                "/input[@class='total_rub'][../label[@for='connection-sum']]"));
        sum.sendKeys(inputData.get("sum"));

        WebElement continueButton = chromeDriver.findElement(By.xpath("//form[@class='pay-form opened']/button[contains(text(), " +
                "'Продолжить')]"));
        continueButton.click();
    }

    public void waitForVisibilityOfFrame() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(IFRAME_XPATH)));
    }

    public void waitForVisibilityOfFrameThroughAttribute() {
        wait.until(ExpectedConditions.attributeToBe(By.xpath(IFRAME_XPATH), "style", "visibility: visible;"));
    }

    public WebElement getIframe() {
        return chromeDriver.findElement(By.xpath(IFRAME_XPATH));
    }

    public String getPlaceHolderAsString(String partOfXpath) {
        WebElement webElement = chromeDriver.findElement(By.xpath("//input[@id='" + partOfXpath + "']"));
        return webElement.getAttribute("placeholder");
    }

    public void switchTheFrame() {
        chromeDriver.switchTo().frame(getIframe());
    }

    public void waitForSpanInFrame() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[contains(text(), 'BYN')]")));
    }

    /*
    Метод извлекает и обрабатывает текстовые данные со страницы
     */
    public String stringModifier(String xPath, String regex, String replacement) {
        WebElement webElement = chromeDriver.findElement(By.xpath(xPath));
        return webElement.getText().replaceAll(regex, replacement);
    }
}
