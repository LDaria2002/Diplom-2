package userPack;

import data.User;
import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;

import static data.Uri.*;
import static io.restassured.RestAssured.given;

public class ChangeUserDataSteps {
    String accessToken;


    @Step("Авторизация для получения токена")
    public String createUserWithToken(User user) {
        accessToken = given()
                .contentType(ContentType.JSON)
                .baseUri(BASE_URI)
                .body(user)
                .when()
                .post(CREATE_USER)
                .path("accessToken");
        return accessToken;
    }

    @Step("GET запрос для получения информации по пользователю")
    public ValidatableResponse getUserData(User user) {
        return
                given()
                        .contentType(ContentType.JSON)
                        .baseUri(BASE_URI)
                        .headers("Authorization", accessToken)
                        .body(user)
                        .when()
                        .get(BASE_USER)
                        .then();
    }

    public ValidatableResponse changeUserData(User user) {
        // Обновление данных пользователя
        return given()
                .contentType(ContentType.JSON)
                .baseUri(BASE_URI)
                .headers("Authorization", accessToken)
                .body(user)
                .when()
                .patch(BASE_USER)
                .then();

    }
    public ValidatableResponse changeUserData401(User user) {
        String accessToken = "null";
        return given()
                .contentType(ContentType.JSON)
                .baseUri(BASE_URI)
                .headers("Authorization", accessToken)
                .body(user)
                .when()
                .patch(BASE_USER)
                .then();
    }
}


