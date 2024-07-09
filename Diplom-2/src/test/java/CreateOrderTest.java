import UserPack.CreateUserSteps;
import UserPack.DeleteUserStep;
import UserPack.LoginUserSteps;
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

import static org.hamcrest.Matchers.is;

public class CreateOrderTest {
    String name = RandomStringUtils.randomAlphabetic(10);
    String password = RandomStringUtils.randomAlphabetic(10);
    String email = RandomStringUtils.randomAlphabetic(10);
    private String accessToken;
    private Order order;
    OrderGenerator orderGenerator = new OrderGenerator ();

    @Before
    public void setUp() {
        order = orderGenerator.getListOrder();
    }


    @Test
    @Description("Создание заказа с авторизацией и ингредиентами")
    public void createOrderWithAuthTest(){
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
        Response responseRegister =
                 new CreateUserSteps()
                .createUser(email +"@yandex.ru", password, name);
        new LoginUserSteps()
                .loginStep(email +"@yandex.ru", password, name);

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
        new CreateUserSteps()
                .createUser(email + "@yandex.ru", password, name);

        new LoginUserSteps()
                .loginStep(email + "@yandex.ru", password, name);
        ValidatableResponse responseRegister =
                new CreateOrderSteps()
                        .createOrderNoHash()
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
        new CreateUserSteps()
                .createUser(email + "@yandex.ru", password, name);

        new LoginUserSteps()
                .loginStep(email + "@yandex.ru", password, name);
        ValidatableResponse responseRegister
                = new CreateOrderSteps()
                .createOrderUnvalidHash()
                .then()
                .statusCode(500);

    }
    @After
    @Step("Очистка тестовых данных")
    public void tearDown() {
        if (accessToken == null) return;
        new DeleteUserStep()
                .delete(accessToken);
    }

}

