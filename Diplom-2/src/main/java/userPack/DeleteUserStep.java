package userPack;

import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;

import static data.Uri.*;
import static io.restassured.RestAssured.given;

public class DeleteUserStep {
    @Step("Удаление пользователя. Send delete request to api/auth/user")
    public  ValidatableResponse delete(String accessToken) {
        return given()
                .contentType(ContentType.JSON)
                .baseUri(BASE_URI)
                .headers("Authorization", accessToken)
                .delete(BASE_USER)
                .then();
    }
}