package by.kufar.api;

import net.datafaker.Faker;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@Execution(ExecutionMode.CONCURRENT)
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
public class LoginTest {
    private static final Logger logger = LogManager.getLogger(LoginTest.class);
    final String LOGIN_URL = "https://cre-auth.kufar.by/v2/auth/signin?token_type=user";

    @BeforeEach
    public void setUp(TestInfo testInfo) {
        String testName = testInfo.getTestMethod().get().getName();
        ThreadContext.put("logFileName", testName);
        logger.info("Старт теста: {}", testName);
    }

    @Test
    public void testLoginWithEmptyBody() {
        logger.info("Выполняю апи логин с пустым боди...");
        String body = "{\n" +
                "    \"login\": \"\",\n" +
                "    \"password\": \"\"\n" +
                "}";
        given()
                .header("content-type", "application/json;charset=UTF-8")
                .body(body)
                .when()
                .post(LOGIN_URL)
                .then()
                .log().all()
                .statusCode(400)
                //.body("errors.password[0]", equalTo("Введите пароль"))
                .body("label.text", equalTo("Не заполнено обязательное поле"));
    }

    @Test
    public void testLoginWithoutPassword() {
        logger.info("Выполняю апи логин без пароя...");
        Faker faker = new Faker();
        String emiL = faker.internet().emailAddress();

        String body = "{\n" +
                "    \"login\": \"" + emiL + "\",\n" +
                "    \"password\": \"\"\n" +
                "}";
        given()
                .header("content-type", "application/json;charset=UTF-8")
                .body(body)
                .when()
                .post(LOGIN_URL)
                .then()
                .statusCode(400)
                .body("label.text", equalTo("Не заполнено обязательное поле"));
    }

    @Test
    public void testLoginWithoutEmail() {
        logger.info("Выполняю апи логин без мыла...");
        Faker faker = new Faker();
        String password = faker.internet().password();

        String body = "{\n" +
                "    \"login\": \"\",\n" +
                "    \"password\": \"" + password + "\"\n" +
                "}";
        ;
        given()
                .header("content-type", "application/json;charset=UTF-8")
                .body(body)
                .when()
                .post(LOGIN_URL)
                .then()
                .statusCode(400)
                .body("label.text", equalTo("Не заполнено обязательное поле"));
    }

    @AfterEach
    public void tearDown() {
        logger.info("Тест завершен.");
        ThreadContext.clearAll();
    }
}
