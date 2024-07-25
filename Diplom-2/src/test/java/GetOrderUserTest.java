import data.User;
import userPack.CreateUserSteps;
import userPack.DeleteUserStep;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.response.ValidatableResponse;
import orderPack.GetOrderUserSteps;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static userPack.UserGenerator.getRandomUser;

public class GetOrderUserTest {

    private String accessToken;
    private User user;

    @Test
    @Description("Получить список заказов авторизованного пользователя")
    public void shouldSuccessTrue(){
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
        user = getRandomUser();
        new CreateUserSteps()
                .createUser(user);
        ValidatableResponse responseRegister =
                new GetOrderUserSteps()
                        .getOrder(user)
                        .statusCode(200)
                        .body("success", is(true));
        accessToken = responseRegister
                .extract()
                .path("accessToken");

    }

    @Test
    @Description("Получить список заказов НЕавторизованного пользователя")
    public void should401StatusCode(){
        ValidatableResponse responseRegister =
                new GetOrderUserSteps()
                        .getOrder401()
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