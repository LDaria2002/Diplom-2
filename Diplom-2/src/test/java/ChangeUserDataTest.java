import UserPack.ChangeUserDataSteps;
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

public class ChangeUserDataTest {
    String name = RandomStringUtils.randomAlphabetic(10);
    String password = RandomStringUtils.randomAlphabetic(10);
    String email = RandomStringUtils.randomAlphabetic(10);
    private String accessToken;

    ChangeUserDataSteps changeUserDataSteps = new ChangeUserDataSteps ();
    @Test
    @Description("Меняю name пользователя")
    public void changeNameTest(){
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());

        changeUserDataSteps.createUserWithToken(email+"@yandex.ru", password, name);
        changeUserDataSteps.getUserData(email+"@yandex.ru", password, name)
                .statusCode(200)
                .body("success", is(true));

        ValidatableResponse responseRegister =
                changeUserDataSteps.changeUserData(email+"@yandex.ru", password, "name");
        accessToken = responseRegister
                .extract()
                .path("accessToken");
    }

    @Test
    @Description("Меняю email пользователя")
    public void changeEmailTest(){
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());

        changeUserDataSteps.createUserWithToken(email+"@yandex.ru", password, name);
        changeUserDataSteps.getUserData(email+"@yandex.ru", password, name)
                .statusCode(200)
                .body("success", is(true));
        ValidatableResponse responseRegister =
                changeUserDataSteps.changeUserData("email@yandex.ru", password, name);
        accessToken = responseRegister
                .extract()
                .path("accessToken");
    }
    @Test
    @Description("Меняю password пользователя")
    public void changePasswordTest() {
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());

        changeUserDataSteps.createUserWithToken(email + "@yandex.ru", password, name);
        changeUserDataSteps.getUserData(email + "@yandex.ru", password, name)
                .statusCode(200)
                .body("success", is(true));

        ValidatableResponse responseRegister =
                changeUserDataSteps.changeUserData(email + "@yandex.ru", "password", name);
        accessToken = responseRegister
                .extract()
                .path("accessToken");
    }
    @Test
    @Description("Меняю password пользователя без авторизации")
    public void changeData401Test() {
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
        ValidatableResponse responseRegister =
                changeUserDataSteps.changeUserData401(password, name);
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

