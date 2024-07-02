package by.mts;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Tests extends BaseTests {
    @Test
    @DisplayName("Проверка названия указанного блока")
    public void checkTheTitleOfBlock() {
        chromeDriver.get("http://mts.by");
        clickCookieButton();
        WebElement title = chromeDriver.findElement(By.xpath("//h2[contains(text(), 'Онлайн пополнение')]"));
        String expected = "Онлайн пополнение\n" +
                "без комиссии";
        Assertions.assertEquals(title.getText(), expected, "Заголовок не соответствует требуемому");
    }

    @ParameterizedTest
    @DisplayName("Проверка наличия логотипов платёжных систем")
    @ValueSource(strings = {"Visa", "Verified By Visa", "MasterCard", "MasterCard Secure Code", "Белкарт"})
    public void checkTheLogoOfBlock(String input) {
        chromeDriver.get("http://mts.by");
        clickCookieButton();
        List<WebElement> logoList = chromeDriver.findElements(By.xpath("//img[contains(@src, " +
                "'/local/templates/new_design/assets/html/images/pages/index/pay')]"));
        List<String> logoNames = logoList.stream()
                .map(webElement -> webElement.getAttribute("alt"))
                .collect(Collectors.toList());
        Assertions.assertTrue(logoNames.contains(input), "На странице отсутствуют требуемое изображение: " + input);
    }

    @Test
    @DisplayName("Проверка работы ссылки «Подробнее о сервисе»")
    public void checkTheLink() {
        chromeDriver.get("http://mts.by");
        clickCookieButton();
        String startPage = chromeDriver.getCurrentUrl();
        WebElement button = chromeDriver.findElement(By.xpath("//a[contains(text(), 'Подробнее о сервисе')]"));
        button.click();
        String newPage = chromeDriver.getCurrentUrl();
        Assertions.assertNotEquals(startPage, newPage, "Ссылка 'Подробнее о сервисе' нерабочая");
    }

    @ParameterizedTest
    @DisplayName("Проверка работы кнопки «Продолжить» (проверяем только вариант «Услуги связи», номер для теста 297777777)")
    @MethodSource("provideMapData")
    public void checkInputData(Map<String, String> inputData) {
        chromeDriver.get("http://mts.by");
        clickCookieButton();
        fillCommunicationServices(inputData);
        WebDriverWait wait = new WebDriverWait(chromeDriver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//iframe[@class='bepaid-iframe']")));
        Assertions.assertTrue(chromeDriver.findElement(By.xpath("//iframe[@class='bepaid-iframe']")).isDisplayed(),
                "Кнопка 'Продолжить' не работает");
    }

    @ParameterizedTest
    @DisplayName("""
            Проверка надписи в незаполненных полях каждого варианта оплаты услуг:\s
            услуги связи,\s
            домашний интернет,\s
            рассрочка,\s
            задолженность;""")
    @MethodSource("providePlaceHolders")
    public void checkPlaceHolders(String option, String phoneXpath, String phoneNumberExpected, String sumXpath,
                                  String sumExpected, String emailXpath, String emailExpected) {
        chromeDriver.get("http://mts.by");
        clickCookieButton();
        WebElement listButton = chromeDriver.findElement(By.xpath("//button[@class='select__header']"));
        listButton.click();
        WebElement variantButton = chromeDriver.findElement(By.xpath("//p[contains(text(), '" + option + "')]"));
        variantButton.click();

        /*
        Проверка плэйсхолдера в поле номера телефона
         */
        checkPlaceHolder(phoneXpath, phoneNumberExpected);
         /*
        Проверка плэйсхолдера в поле введённой суммы
         */
        checkPlaceHolder(sumXpath, sumExpected);
         /*
        Проверка плэйсхолдера в поле электронной почты
         */
        checkPlaceHolder(emailXpath, emailExpected);
    }

    @ParameterizedTest
    @DisplayName("проверить корректность отображения суммы (в том числе на кнопке)," +
            " номера телефона, " +
            "а также надписей в незаполненных полях для ввода реквизитов карты, наличие иконок платёжных систем.")
    @MethodSource("provideMapData")
    public void checkPopUp(Map<String, String> inputData) {
        chromeDriver.get("http://mts.by");
        clickCookieButton();
        fillCommunicationServices(inputData);

        WebDriverWait wait = new WebDriverWait(chromeDriver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.attributeToBe(By.xpath("//iframe[@class='bepaid-iframe']"), "style", "visibility: visible;"));

        WebElement frame = chromeDriver.findElement(By.xpath("//iframe[@class='bepaid-iframe']"));
        chromeDriver.switchTo().frame(frame);

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[contains(text(), 'BYN')]")));
        /*
        Проверка корректности отображения суммы в pop-up
         */
        Assertions.assertEquals(inputData.get("sum"), stringModifier("//span[contains(text(), 'BYN')]",
                "\\.00 BYN", ""), "Placeholder " + inputData.get("sum") +
                " отсутствует или содержит неверные данные");

        /*
        Проверка корректности отображения суммы на кнопке в pop-up
         */
        Assertions.assertEquals(inputData.get("sum"), stringModifier("//button[contains(text(), 'BYN')]",
                "Оплатить |.00 BYN", ""), "Placeholder " + inputData.get("sum") +
                " отсутствует или содержит неверные данные");

        /*
        Проверка корректности отображения номера телефона в pop-up
         */
        Assertions.assertEquals(inputData.get("phone"), stringModifier("//span[contains(text(), 'Номер')]",
                ".*?(297777777).*", "$1"), "Placeholder " + inputData.get("phone") +
                " отсутствует или содержит неверные данные");

        /*
        Проверка корректности отображения плэйсхоледра в поле 'Номер карты' в pop-up
         */
        WebElement placeHolderCardNumber = chromeDriver.findElement(By.xpath("//label[contains(text(), 'Номер карты')]"));
        Assertions.assertEquals("Номер карты", placeHolderCardNumber.getText(), "Placeholder " +
                "'Номер карты' неверный либо отсутствует");

        /*
        Проверка корректности отображения плэйсхоледра в поле 'Срок действия' в pop-up
         */
        WebElement placeHolderValidityPeriod = chromeDriver.findElement(By.xpath("//label[contains(text(), 'Срок действия')]"));
        Assertions.assertEquals("Срок действия", placeHolderValidityPeriod.getText(), "Placeholder " +
                "'Срок действия' неверный либо отсутствует");

        /*
        Проверка корректности отображения плэйсхоледра в поле 'CVC' в pop-up
         */
        WebElement placeHolderCVC = chromeDriver.findElement(By.xpath("//label[contains(text(), 'CVC')]"));
        Assertions.assertEquals("CVC", placeHolderCVC.getText(), "Placeholder 'CVC' неверный либо отсутствует");

        /*
        Проверка корректности отображения плэйсхоледра в поле 'Имя держателя' в pop-up
         */
        WebElement nameOfCardHolder = chromeDriver.findElement(By.xpath("//label[contains(text(), 'Имя держателя (как на карте)')]"));
        Assertions.assertEquals("Имя держателя (как на карте)", nameOfCardHolder.getText(), "Placeholder" +
                " 'Имя держателя (как на карте)' неверный либо отсутствует");

        /*
        Проверка наличия иконок платёжных систем в pop-up
         */
        List<String> expectedIcons = Arrays.asList("mastercard-system.svg",
                "visa-system.svg",
                "belkart-system.svg",
                "mir-system-ru.svg",
                "maestro-system.svg");
        List<WebElement> icons = chromeDriver.findElements(By.xpath("//div[@class='cards-brands cards-brands__container ng-tns-c61-0 ng-trigger ng-trigger-brandsState ng-star-inserted']//img"));
        List<String> iconSources = icons.stream()
                .map(element -> element.getAttribute("src"))
                .collect(Collectors.toList());
        Assertions.assertTrue(iconSources.stream().allMatch(source -> expectedIcons.stream().anyMatch(source::contains)));
    }


    /*
    Метод извлекает и обрабатывает текстовые данные со страницы
     */
    private String stringModifier(String xPath, String regex, String replacement) {
        WebElement webElement = chromeDriver.findElement(By.xpath(xPath));
        String modifiedString = webElement.getText().replaceAll(regex, replacement);
        return modifiedString;
    }

    /*
    Метод разрешает сохранять куки
     */
    private void clickCookieButton() {
        WebElement cookieButton = chromeDriver.findElement(By.xpath("//button[@class='btn btn_black cookie__ok']"));
        cookieButton.click();
    }

    /*
    Универсальный метод для проверки плэйсхолдера
     */
    public void checkPlaceHolder(String xpath, String valueExpected) {
        WebElement webElement = chromeDriver.findElement(By.xpath("//input[@id='" + xpath + "']"));
        String placeholder = webElement.getAttribute("placeholder");
        Assertions.assertEquals(valueExpected, placeholder, "Атрибут placeholder не равен '" +
                valueExpected + "'");
    }

    /*
    Метод для заполнения анкеты в блоке "Услуги связи"
     */
    private void fillCommunicationServices(Map<String, String> inputData) {
        WebElement phoneNumber =
                chromeDriver.findElement(By.xpath("//div[@class='input-wrapper input-wrapper_label-left']" +
                        "/input[@class='phone'][../label[@for='connection-phone']]"));
        phoneNumber.sendKeys(inputData.get("phone"));
        WebElement sum =
                chromeDriver.findElement(By.xpath("//div[@class='input-wrapper input-wrapper_label-right']" +
                        "/input[@class='total_rub'][../label[@for='connection-sum']]"));
        sum.sendKeys(inputData.get("sum"));
        WebElement continueButton =
                chromeDriver.findElement(By.xpath("//form[@class='pay-form opened']/button[contains(text(), " +
                        "'Продолжить')]"));
        continueButton.click();
    }

    /*
    Наборы аргументов для проверки каждого варианта оплаты услуг:
    1. Вариант услуги
    2. Фрагмент xPath для поля номера телефона
    3. Ожидаемое содержимое Placeholder для поля номера телефона
    4. Фрагмент xPath для поля вводимой суммы
    5. Ожидаемое содержимое Placeholder для поля суммы
    6. Фрагмент xPath для поля электронной почты
    7. Ожидаемое содержимое Placeholder для поля электронной почты
     */
    private static Stream<Arguments> providePlaceHolders() {
        return Stream.of(
                Arguments.arguments("Услуги связи",
                        "connection-phone", "Номер телефона",
                        "connection-sum", "Сумма",
                        "connection-email", "E-mail для отправки чека"),
                Arguments.arguments("Домашний интернет",
                        "internet-phone", "Номер абонента",
                        "internet-sum", "Сумма",
                        "internet-email", "E-mail для отправки чека"),
                Arguments.arguments("Рассрочка",
                        "score-instalment", "Номер счета на 44",
                        "instalment-sum", "Сумма",
                        "instalment-email", "E-mail для отправки чека"),
                Arguments.arguments("Задолженность",
                        "score-arrears", "Номер счета на 2073",
                        "arrears-sum", "Сумма",
                        "arrears-email", "E-mail для отправки чека")
        );
    }

    /*
    Вводимые данные в блоке "Услуги связи"
    Метод создан с целью повторного использования данных в различных тестах
     */
    private static Stream<Map<String, String>> provideMapData() {
        Map<String, String> inputData = Map.of("phone", "297777777", "sum", "100");
        return Stream.of(inputData);
    }
}
