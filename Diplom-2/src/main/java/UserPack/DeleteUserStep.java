package UserPack;

import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class DeleteUserStep {
    @Step("Удаление пользователя. Send delete request to api/auth/user")
    public  ValidatableResponse delete(String accessToken) {
        return given()
                .contentType(ContentType.JSON)
                .baseUri("https://stellarburgers.nomoreparties.site")
                .headers("Authorization", accessToken)
                .delete("/api/auth/user")
                .then();
    }
}