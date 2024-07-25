package orderPack;
import data.Order;
import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;

import static data.Uri.*;
import static io.restassured.RestAssured.given;

public class CreateOrderSteps {

    @Step("метод создания заказа с хэшем")
    public Response createOrder(Order order,String accessToken ) {
              return  given()
                        .contentType(ContentType.JSON)
                        .baseUri(BASE_URI)
                        .headers("Authorization", accessToken)
                .body(order)
                .when()
                .post(USER_ORDERS);
    }

    @Step("метод создания заказа без хэша")
    public Response createOrderNoHash(Order order){
        return  given()
                .contentType(ContentType.JSON)
                .baseUri(BASE_URI)
                .body(order)
                .when()
                .post(USER_ORDERS);
    }
    @Step("метод создания заказа c неверным хэшем")
    public Response createOrderUnvalidHash(Order order){
        return  given()
                .contentType(ContentType.JSON)
                .baseUri(BASE_URI)
                .body(order)
                .when()
                .post(USER_ORDERS);
    }

    @Step("Получение данных об ингредиентах ")
    public static ValidatableResponse getAllIngredients () {
        return given()
                .contentType(ContentType.JSON)
                .baseUri(BASE_URI)
                .when()
                .get(ALL_INGREDIENT)
                .then();
    }
}

