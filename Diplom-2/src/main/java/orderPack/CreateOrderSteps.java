package orderPack;

import data.Order;
import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class CreateOrderSteps {

    @Step("метод создания заказа с хэшем")
    public Response createOrder(Order order,String accessToken ) {
              return  given()
                        .contentType(ContentType.JSON)
                        .baseUri("https://stellarburgers.nomoreparties.site")
                        .headers("Authorization", accessToken)
                .body(order)
                .when()
                .post("/api/orders");
    }

    @Step("метод создания заказа без хэша")
    public Response createOrderNoHash(){
        return  given()
                .contentType(ContentType.JSON)
                .baseUri("https://stellarburgers.nomoreparties.site")
                .body("{\n" +
                        "  \"ingredients\": []\n" +
                        "}")
                .when()
                .post("/api/orders");
    }
    @Step("метод создания заказа c неверным хэшем")
    public Response createOrderUnvalidHash(){
        return  given()
                .contentType(ContentType.JSON)
                .baseUri("https://stellarburgers.nomoreparties.site")
                .body("{\n" +
                        "  \"ingredients\": [\"60d3b4143434343443acab0026a733c6\",\"609646e4dc44444444916e00276b2870\"]\n" +
                        "}")
                .when()
                .post("/api/orders");
    }

    @Step("Получение данных об ингредиентахю Send get request to api/ingredients/")
    public static ValidatableResponse getAllIngredients () {
        return given()
                .contentType(ContentType.JSON)
                .baseUri("https://stellarburgers.nomoreparties.site")
                .when()
                .get("api/ingredients")
                .then();
    }
}

