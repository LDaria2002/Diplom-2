import data.User;
import org.junit.Before;
import userPack.ChangeUserDataSteps;
import userPack.DeleteUserStep;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.response.ValidatableResponse;

import org.junit.After;
import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static userPack.UserGenerator.getRandomUser;

public class ChangeUserDataTest {
    private String accessToken;
    private User user;

    ChangeUserDataSteps changeUserDataSteps = new ChangeUserDataSteps ();
    private User secondUser;


    @Test
    @Description("Меняю данные пользователя")
    public void changeNameTest(){
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
 user = getRandomUser();
        changeUserDataSteps.createUserWithToken(user);
        changeUserDataSteps.getUserData(user)
                .statusCode(200)
                .body("success", is(true));

       secondUser = getRandomUser();
        ValidatableResponse responseRegister =
                changeUserDataSteps.changeUserData(secondUser);
        accessToken = responseRegister
                .extract()
                .path("accessToken");
    }

    @Test
    @Description("Меняю данные пользователя без авторизации")
    public void changeData401Test() {
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
        user = getRandomUser();
        ValidatableResponse responseRegister =
                changeUserDataSteps.changeUserData401(user);
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

