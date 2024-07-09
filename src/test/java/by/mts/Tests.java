package by.mts;

import io.qameta.allure.Epic;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import ru.netology.helpers.ExpectedData;
import ru.netology.helpers.Links;

import java.util.Map;
import java.util.stream.Stream;

import static ru.netology.steps.Steps.*;

public class Tests extends BaseTests {
    @Epic(value = "Задание L15")
    @DisplayName("Проверка названия заголовка")
    @Test
    public void checkTheTitleOfBlock() {
        comeInSiteAndAcceptCookie(chromeDriver, Links.MTS_LINK);
        checkTheTitle(ExpectedData.EXPECTED_TITLE_OF_MTS_MAIN_PAGE);

    }

    @Epic(value = "Задание L15")
    @DisplayName("Проверка наличия логотипов платёжных систем")
    @ParameterizedTest(name = "{displayName} : {arguments}")
    @ValueSource(strings = {ExpectedData.VISA, ExpectedData.VERIFIED_BY_VISA, ExpectedData.MASTER_CARD,
            ExpectedData.MASTER_CARD_SECURE_CODE, ExpectedData.BEL_CARD})
    public void checkTheLogoOfBlock(String input) {
        comeInSiteAndAcceptCookie(chromeDriver, Links.MTS_LINK);
        checkTheLogoByName(input);
        checkTheLogoIsDisplayed();
    }

    @Epic(value = "Задание L15")
    @DisplayName("Проверка работы ссылки «Подробнее о сервисе»")
    @Test
    public void checkTheLink() {
        comeInSiteAndAcceptCookie(chromeDriver, Links.MTS_LINK);
        checkTheButtonInDetails(Links.MTS_LINK);
    }

    @Epic(value = "Задание L15")
    @DisplayName("Проверка работы кнопки «Продолжить» (проверяем только вариант «Услуги связи», номер для теста 297777777)")
    @ParameterizedTest(name = "{displayName} : {arguments}")
    @MethodSource("provideMapData")
    public void checkInputData(Map<String, String> inputData) {
        comeInSiteAndAcceptCookie(chromeDriver, Links.MTS_LINK);
        fillTheForm(inputData);
        checkTheButtonContinue();

    }

    @Epic(value = "Задание L16")
    @DisplayName("Проверка надписи в незаполненных полях каждого варианта оплаты услуг")
    @ParameterizedTest(name = "{displayName} : {0}")
    @MethodSource("providePlaceHolders")
    public void checkPlaceHolders(String option, String phoneXpath, String phoneNumberExpected, String sumXpath,
                                  String sumExpected, String emailXpath, String emailExpected) {
        comeInSiteAndAcceptCookie(chromeDriver, Links.MTS_LINK);
        chooseTheService(option);
        checkThePlaceHolderInForm(phoneXpath, phoneNumberExpected);
        checkThePlaceHolderInForm(sumXpath, sumExpected);
        checkThePlaceHolderInForm(emailXpath, emailExpected);
    }

    @Epic(value = "Задание L16")
    @DisplayName("Проверка корректность отображения суммы (в том числе на кнопке)," +
            " номера телефона, " +
            "а также надписей в незаполненных полях для ввода реквизитов карты, наличие иконок платёжных систем.")
    @ParameterizedTest(name = "{displayName} : {arguments}")
    @MethodSource("provideMapData")
    public void checkPopUp(Map<String, String> inputData) {
        comeInSiteAndAcceptCookie(chromeDriver, Links.MTS_LINK);
        fillTheForm(inputData);
        switchToAnotherFrame();
        testTheSumInPopUp(inputData);
        testTheSumAtPopUpButton(inputData);
        testThePhoneNumberInPopUp(inputData);
        testTheCardNumberInPopUp();
        testTheValidationPeriodInPopUp();
        testCVCInPopUp();
        testCardHolderNameInPopUp();
        testCardIconsInPopUp();
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
