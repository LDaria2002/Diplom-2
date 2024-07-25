import data.User;
import user.CreateUserSteps;
import user.DeleteUserStep;
import user.LoginUserSteps;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static user.UserGenerator.getRandomUser;

public class LoginUserTest {

    private String accessToken;
    private User user;
    @Test
    @Description("Авторизация под существующим пользователем")
    public void shouldSuccessTrue() {
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
        user = getRandomUser();
        ValidatableResponse responseRegister =
                new CreateUserSteps()
                        .createUser(user)
                        .then()
                        .statusCode(200)
                        .body("success", is(true));

                new LoginUserSteps()
                        .loginStep(user)
                        .then()
                        .statusCode(200)
                        .body("success", is(true));
        accessToken = responseRegister
                .extract()
                .path("accessToken");
    }

    @Test
    @Description("Авторизация с неправильными данными для существующего пользователя")
    public void shouldReturn401() {
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
        user = getRandomUser();
        ValidatableResponse responseRegister =
                new CreateUserSteps()
                .createUser(user)
                        .then()
                        .statusCode(200)
                        .body("success", is(true));

        user.setPassword("");
                new LoginUserSteps()
                        .loginStep(user)
                        .then()
                        .statusCode(401)
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

