import UserPack.CreateUserSteps;
import UserPack.DeleteUserStep;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.response.ValidatableResponse;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Test;

import static org.hamcrest.Matchers.is;

public class CreateUserTest {

    String name = RandomStringUtils.randomAlphabetic(10);
    String password = RandomStringUtils.randomAlphabetic(10);
    String email = RandomStringUtils.randomAlphabetic(10);
    private String accessToken;

    @Test
    @Description("Создаю пользователя")
    public void shouldReturnOk() {
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
        ValidatableResponse responseRegister =
                new CreateUserSteps()
                        .createUser(email + "@yandex.ru", password, name)
                        .then()
                        .statusCode(200)
                        .body("success", is(true));

        accessToken = responseRegister
                .extract()
                .path("accessToken");
    }


    @Test
    @Description("Создаю существующего пользователя")
    public void tryCreateOldUser() {
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
        ValidatableResponse responseRegister =
                new CreateUserSteps()
                        .createUser("test-data@yandex.ru", "password", "name")
                        .then()
                        .statusCode(403)
                        .body("success", is(false));

        accessToken = responseRegister
                .extract()
                .path("accessToken");
    }

    @Test
    @Description("Создаю пользователя без заполненного обязательного поля")
    public void tryCreateUserWithoutPassword() {
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
        ValidatableResponse responseRegister =
                new CreateUserSteps()
                        .createUser("test-data@yandex.ru", null, "name")
                        .then()
                        .statusCode(403)
                        .body("success", is(false));

        accessToken = responseRegister
                .extract()
                .path("accessToken");
    }

    @After
    @Step("Очистка тестовых данных")
    public void tearDown() {
        if (accessToken == null) return;
        new DeleteUserStep()
                .delete(accessToken);

    }
}