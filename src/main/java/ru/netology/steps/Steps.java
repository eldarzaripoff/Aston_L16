package ru.netology.steps;

import io.qameta.allure.Step;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import ru.netology.helpers.ExpectedData;
import ru.netology.helpers.XPaths;
import ru.netology.pages.MTSMainPage;
import ru.netology.pages.MTSPageWithFrame;

import java.util.Map;

public class Steps {
    static WebDriver chromeDriver;

    @Step("Вход на сайт {link} и принятие Политики куки файлов")
    public static void comeInSiteAndAcceptCookie(WebDriver currentDriver, String link) {
        chromeDriver = currentDriver;
        chromeDriver.get(link);
        MTSMainPage mtsMainPage = new MTSMainPage(chromeDriver);
        mtsMainPage.getCookieButton().click();
    }

    @Step("Проверка названия заголовка")
    public static void checkTheTitle(String expectedTitleName) {
        MTSMainPage mtsMainPage = new MTSMainPage(chromeDriver);
        Assertions.assertEquals(
                expectedTitleName,
                mtsMainPage.getTitle().getText(),
                "Фактическое название заголовка: " + mtsMainPage.getTitle().getText() +
                        " не соответствует требуемому: " + expectedTitleName);
    }

    @Step("Проверка наличия логотипов платёжных систем: сопоставление наименования логотипа и наименования веб-элемента")
    public static void checkTheLogoByName(String input) {
        MTSMainPage mtsMainPage = new MTSMainPage(chromeDriver);
        Assertions.assertTrue(
                mtsMainPage.getLogoListAsListOfStrings().contains(input),
                "На странице отсутствуют требуемое изображение: " + input);
    }

    @Step("Проверка наличия логотипов платёжных систем: проверка веб-элемента на видимость")
    public static void checkTheLogoIsDisplayed() {
        MTSMainPage mtsMainPage = new MTSMainPage(chromeDriver);
        Assertions.assertTrue(mtsMainPage.getLogoList().stream().allMatch(WebElement::isDisplayed),
                "На странице отсутствуют требуемое изображение");
    }

    @Step("Проверка работоспособности перехода по ссылке «Подробнее о сервисе»")
    public static void checkTheButtonInDetails(String mtsLink) {
        MTSMainPage mtsMainPage = new MTSMainPage(chromeDriver);
        mtsMainPage.getButtonInDetails().click();
        Assertions.assertNotEquals(mtsLink,
                mtsMainPage.getCurrentUrl(),
                "Ссылка 'Подробнее о сервисе' нерабочая");
    }

    @Step("Заполнение анкеты в блоке 'Услуги связи'")
    public static void fillTheForm(Map<String, String> inputData) {
        MTSMainPage mtsMainPage = new MTSMainPage(chromeDriver);
        mtsMainPage.fillCommunicationServices(inputData);
    }

    @Step("Проверка работы кнопки «Продолжить»")
    public static void checkTheButtonContinue() {
        MTSMainPage mtsMainPage = new MTSMainPage(chromeDriver);
        mtsMainPage.waitForVisibilityOfFrame();
        Assertions.assertTrue(mtsMainPage.getIframe().isDisplayed(),
                "Кнопка 'Продолжить' не работает");
    }

    @Step("Выбор услуги")
    public static void chooseTheService(String option) {
        MTSMainPage mtsMainPage = new MTSMainPage(chromeDriver);
        mtsMainPage.getListUnrollButton().click();
        mtsMainPage.getButtonForSelectOptionInList(option).click();
    }

    @Step("Проверка наличия плэйсхолдера в поле {variableExpected}")
    public static void checkThePlaceHolderInForm(String xpath, String variableExpected) {
        MTSMainPage mtsMainPage = new MTSMainPage(chromeDriver);
        Assertions.assertTrue(mtsMainPage.getPlaceHolderAsString(xpath).contains(variableExpected),
                "Плэйсхолдер в поле" + variableExpected + " отсуствует");
    }

    @Step("Переход на фрейм с результатами заполнения анкеты")
    public static void switchToAnotherFrame() {
        MTSMainPage mtsMainPage = new MTSMainPage(chromeDriver);
        mtsMainPage.waitForVisibilityOfFrameThroughAttribute();
        mtsMainPage.switchTheFrame();
        mtsMainPage.waitForSpanInFrame();
    }

    @Step("Проверка корректности отображения суммы в pop-up")
    public static void testTheSumInPopUp(Map<String, String> inputData) {
        MTSPageWithFrame mtsPageWithFrame = new MTSPageWithFrame(chromeDriver);
        Assertions.assertEquals(inputData.get("sum"), mtsPageWithFrame.stringModifier(
                        XPaths.SPAN_CONTAINS_TEXT_BYN_XPATH, "\\.00 BYN", ""),
                "В pop-up сумма отображается некорректно");
    }

    @Step("Проверка корректности отображения суммы на кнопке в pop-up")
    public static void testTheSumAtPopUpButton(Map<String, String> inputData) {
        MTSPageWithFrame mtsPageWithFrame = new MTSPageWithFrame(chromeDriver);
        Assertions.assertEquals(inputData.get("sum"), mtsPageWithFrame.stringModifier(
                        XPaths.BUTTON_CONTAINS_TEXT_BYN_XPATH, "Оплатить |.00 BYN", ""),
                "На кнопке в pop-up сумма отображается некорректно");
    }

    @Step("Проверка корректности отображения номера телефона в pop-up")
    public static void testThePhoneNumberInPopUp(Map<String, String> inputData) {
        MTSPageWithFrame mtsPageWithFrame = new MTSPageWithFrame(chromeDriver);
        Assertions.assertEquals(inputData.get("phone"),
                mtsPageWithFrame.stringModifier(XPaths.PHONE_NUMBER_XPATH, ".*?(297777777).*", "$1"),
                "Placeholder " + inputData.get("phone") + " отсутствует или содержит неверные данные");
    }

    @Step("Проверка корректности отображения плэйсхоледра в поле 'Номер карты' в pop-up")
    public static void testTheCardNumberInPopUp() {
        MTSPageWithFrame mtsPageWithFrame = new MTSPageWithFrame(chromeDriver);
        Assertions.assertEquals(ExpectedData.EXPECTED_CARD_NUMBER,
                mtsPageWithFrame.getPlaceHolder(XPaths.CARD_NUMBER_XPATH).getText(),
                "Placeholder 'Номер карты' неверный либо отсутствует");
    }

    @Step("Проверка корректности отображения плэйсхоледра в поле 'Срок действия' в pop-up")
    public static void testTheValidationPeriodInPopUp() {
        MTSPageWithFrame mtsPageWithFrame = new MTSPageWithFrame(chromeDriver);
        Assertions.assertEquals(ExpectedData.EXPECTED_VALIDITY_PERIOD,
                mtsPageWithFrame.getPlaceHolder(XPaths.VALIDITY_PERIOD_XPATH).getText(),
                "Placeholder 'Срок действия' неверный либо отсутствует");
    }

    @Step("Проверка корректности отображения плэйсхоледра в поле 'CVC' в pop-up")
    public static void testCVCInPopUp() {
        MTSPageWithFrame mtsPageWithFrame = new MTSPageWithFrame(chromeDriver);
        Assertions.assertEquals(ExpectedData.CVC, mtsPageWithFrame.getPlaceHolder(XPaths.CVC_XPATH).getText(),
                "Placeholder 'CVC' неверный либо отсутствует");
    }

    @Step("Проверка корректности отображения плэйсхоледра в поле 'Имя держателя' в pop-up")
    public static void testCardHolderNameInPopUp() {
        MTSPageWithFrame mtsPageWithFrame = new MTSPageWithFrame(chromeDriver);
        Assertions.assertEquals(ExpectedData.NAME_OF_CARD_HOLDER, mtsPageWithFrame.getPlaceHolder(XPaths.NAME_OF_CARD_XPATH).getText(),
                "Placeholder 'Имя держателя (как на карте)' неверный либо отсутствует");
    }

    @Step("Проверка наличия иконок платёжных систем в pop-up")
    public static void testCardIconsInPopUp() {
        MTSPageWithFrame mtsPageWithFrame = new MTSPageWithFrame(chromeDriver);
        Assertions.assertTrue(mtsPageWithFrame.getIconSourcesAsStrings().stream()
                .allMatch(source -> ExpectedData.EXPECTED_ICONS.stream()
                        .anyMatch(source::contains)));
    }
}
