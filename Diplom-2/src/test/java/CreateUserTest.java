import data.User;
import userPack.CreateUserSteps;
import userPack.DeleteUserStep;
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
import static userPack.UserGenerator.getRandomUser;

public class CreateUserTest {

    private String accessToken;
    private User user;

    @Test
    @Description("Создаю пользователя")
    public void shouldReturnOk() {
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
        user = getRandomUser();
        ValidatableResponse responseRegister =
                new CreateUserSteps()
                        .createUser(user)
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
        user = getRandomUser();
        ValidatableResponse responseRegisterFirst =
                new CreateUserSteps()
                        .createUser(user)
                        .then()
                        .statusCode(200)
                        .body("success", is(true));

        accessToken = responseRegisterFirst
                .extract()
                .path("accessToken");

        ValidatableResponse responseRegisterSecond =
                new CreateUserSteps()
                        .createUser(user)
                        .then()
                        .statusCode(403)
                        .body("success", is(false));

        accessToken = responseRegisterSecond
                .extract()
                .path("accessToken");

    }

    @Test
    @Description("Создаю пользователя без заполненного обязательного поля")
    public void tryCreateUserWithoutPassword() {
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
        user = getRandomUser();
        user.setName("");
        ValidatableResponse responseRegister =
                new CreateUserSteps()
                        .createUser(user)
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