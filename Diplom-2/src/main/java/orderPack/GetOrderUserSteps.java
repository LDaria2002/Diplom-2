package orderPack;

import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class GetOrderUserSteps {
    @Step("Получаем заказы пользователя")
    public ValidatableResponse getOrder(String email, String password, String name) {
        String token =
                given()
                        .contentType(ContentType.JSON)
                        .baseUri("https://stellarburgers.nomoreparties.site")
                        .body("{\n" +
                                "    \"email\": \"" + email + "\",\n" +
                                "    \"password\": \"" + password + "\",\n" +
                                "    \"name\": \"" + name + "\"\n"  +
                                "}")
                        .when()
                        .post("/api/auth/login")
                        .path("accessToken");
        return
                given()
                        .contentType(ContentType.JSON)
                        .baseUri("https://stellarburgers.nomoreparties.site")
                        .headers("Authorization", token)
                        .when()
                        .get("/api/orders")
                        .then();
    }
    @Step ("Пытаемся получить список без авторизации")
    public ValidatableResponse getOrder401(){
        String token = "null";
        return
                given()
                        .contentType(ContentType.JSON)
                        .baseUri("https://stellarburgers.nomoreparties.site")
                        .headers("Authorization", token)
                        .when()
                        .get("/api/orders")
                        .then();
    }
}