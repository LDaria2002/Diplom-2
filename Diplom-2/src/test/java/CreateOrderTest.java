import data.User;
import userPack.CreateUserSteps;
import userPack.DeleteUserStep;
import userPack.LoginUserSteps;
import data.Order;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import orderPack.CreateOrderSteps;
import orderPack.OrderGenerator;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static userPack.UserGenerator.getRandomUser;

public class CreateOrderTest {

    private String accessToken;
    private Order order;
    private User user;
    OrderGenerator orderGenerator = new OrderGenerator ();

    @Before
    public void setUp() {
        order = orderGenerator.getListOrder();
    }


    @Test
    @Description("Создание заказа с авторизацией и ингредиентами")
    public void createOrderWithAuthTest(){
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
        user  = getRandomUser();
        Response responseRegister =
                 new CreateUserSteps()
                .createUser(user);
        new LoginUserSteps()
                .loginStep(user);

        accessToken = responseRegister
                .path("accessToken");

                new CreateOrderSteps()
                        .createOrder(order, accessToken)
                        .then()
                        .statusCode(200)
                        .body("success", is(true));

    }


    @Test
    @Description("Создание заказа без авторизации")
    public void createOrderNoAuthTest(){
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());

                new CreateOrderSteps()
                        .createOrder(order, "Token")
                        .then()
                        .statusCode(200)
                        .body("success", is(true));
    }


    @Test
    @Description("Создание заказа без ингредиентов")
    public void createOrderNoIngredientsTest() {
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
        user  = getRandomUser();
        new CreateUserSteps()
                .createUser(user);

        new LoginUserSteps()
                .loginStep(user);
        order.setIngredients(java.util.Collections.emptyList());
        ValidatableResponse responseRegister =
                new CreateOrderSteps()
                        .createOrderNoHash(order)
                        .then()
                        .statusCode(400);
        accessToken = responseRegister
                .extract()
                .path("accessToken");
    }

    @Test
    @Description("Создание заказа с неверным хешем ингредиентов")
    public void createOrderNoHashTest () {
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
        user  = getRandomUser();
        new CreateUserSteps()
                .createUser(user);

        new LoginUserSteps()
                .loginStep(user);

        List wrongIngridient = new ArrayList();
        wrongIngridient.add("50d3b41abdacab0026a733c9");
        order.setIngredients(wrongIngridient);

        ValidatableResponse responseRegister
                = new CreateOrderSteps()
                .createOrderUnvalidHash(order)
                .then()
                .statusCode(400);
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

