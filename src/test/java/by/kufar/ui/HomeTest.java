package by.kufar.ui;

import by.kufar.HomePage;
import by.kufar.driver.Driver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

@Execution(ExecutionMode.CONCURRENT)
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
public class HomeTest {
    private static final Logger logger = LogManager.getLogger(HomeTest.class);

    @BeforeEach
    public void setupAuth(TestInfo testInfo) {
        String testName = testInfo.getTestMethod().get().getName();
        ThreadContext.put("logFileName", testName);
        logger.info("Старт теста: {}", testName);

        HomePage homePage = new HomePage();
        homePage.open();
        homePage.clickCookies();
    }

    @Test
    public void testCopyRights() {
        logger.info("Выполняю проверку копирайтов...");
        HomePage homePage = new HomePage();
        String textActualCopyRights = homePage.getCopyRights();
        Assertions.assertEquals("Куфар — площадка объявлений Беларуси. ООО «Куфар Тех» включено в " +
                "государственный информационный ресурс «Реестр рекламораспространителей»", textActualCopyRights);
    }

    @Test
    public void testButtonAuthText() {
        logger.info("Выполняю проверку текста кнопки авторизации...");
        HomePage homePage = new HomePage();
        String textActualButtonAuth = homePage.getButtonAuthText();
        Assertions.assertEquals("Войти", textActualButtonAuth);
    }

    @Test
    public void testPlaceholderText() {
        logger.info("Выполняю проверку плейсхолдера поиска...");
        HomePage homePage = new HomePage();
        String checkText = "Поиск объявлений";
        String actualText = homePage.getPlaceholderText();
        Assertions.assertTrue(
                actualText.contains(checkText),
                "Текст '" + checkText + "' не найден в actualText: " + actualText
        );
    }

    @AfterEach
    public void closeDriver() {
        logger.info("Тест завершен. Закрываю браузер.");
        Driver.quitDriver();
        ThreadContext.clearAll();
    }
}
