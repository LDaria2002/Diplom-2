package service;

import data.User;
import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;

import static data.Uri.*;
import static io.restassured.RestAssured.given;

public class GetOrderUserSteps {
    @Step("Получаем заказы пользователя")
    public ValidatableResponse getOrder(User user) {
        String token =
                given()
                        .contentType(ContentType.JSON)
                        .baseUri(BASE_URI)
                        .body(user)
                        .when()
                        .post(LOGIN_USER)
                        .path("accessToken");
        return
                given()
                        .contentType(ContentType.JSON)
                        .baseUri(BASE_URI)
                        .headers("Authorization", token)
                        .when()
                        .get(USER_ORDERS)
                        .then();
    }
    @Step ("Пытаемся получить список без авторизации")
    public ValidatableResponse getOrder401(){
        String token = "null";
        return
                given()
                        .contentType(ContentType.JSON)
                        .baseUri(BASE_URI)
                        .headers("Authorization", token)
                        .when()
                        .get(USER_ORDERS)
                        .then();
    }
}