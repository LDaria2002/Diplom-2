import UserPack.CreateUserSteps;
import UserPack.DeleteUserStep;
import UserPack.LoginUserSteps;
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

public class LoginUserTest {
    String name = RandomStringUtils.randomAlphabetic(10);
    String password = RandomStringUtils.randomAlphabetic(10);
    String email = RandomStringUtils.randomAlphabetic(10);
    private String accessToken;
    @Test
    @Description("Авторизация под существующим пользователем")
    public void shouldSuccessTrue() {
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
        new CreateUserSteps()
                .createUser(email +"@yandex.ru", password, name);

        ValidatableResponse responseRegister =
                new LoginUserSteps()
                        .loginStep(email +"@yandex.ru", password, name)
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
        new CreateUserSteps()
                .createUser(email+"@yandex.ru", password, name);
        ValidatableResponse responseRegister =
                new LoginUserSteps()
                        .loginStep(email+"@yandex.ru", "takogoParolyaNet", name)
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

