package UserPack;

import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import static io.restassured.RestAssured.given;

public class ChangeUserDataSteps {
    String accessToken;


    @Step("Авторизация для получения токена")
    public String createUserWithToken(String email, String password, String name) {
        accessToken = given()
                .contentType(ContentType.JSON)
                .baseUri("https://stellarburgers.nomoreparties.site")
                .body("{\n" +
                        "    \"email\": \"" + email + "\",\n" +
                        "    \"password\": \"" + password + "\",\n" +
                        "    \"name\": \"" + name + "\"\n" +
                        "}")
                .when()
                .post("/api/auth/register")
                .path("accessToken");
        return accessToken;
    }

    @Step("GET запрос для получения информации по пользователю")
    public ValidatableResponse getUserData(String email, String password, String name) {
        return
                given()
                        .contentType(ContentType.JSON)
                        .baseUri("https://stellarburgers.nomoreparties.site")
                        .headers("Authorization", accessToken)
                        .body("{\n" +
                                "   \"email\": \"" + email + "\",\n" +
                                "    \"password\": \"" + password + "\",\n" +
                                "    \"name\": \"" + name + "\"\n" +
                                "}")
                        .when()
                        .get("/api/auth/user")
                        .then();
    }

    public ValidatableResponse changeUserData(String email, String password, String name) {
        // Обновление данных пользователя
        return given()
                .contentType(ContentType.JSON)
                .baseUri("https://stellarburgers.nomoreparties.site")
                .headers("Authorization", accessToken)
                .body("{\n" +
                        "    \"email\": \"AbC@yandex.ru\",\n" +
                        "    \"password\": \"" + password + "\",\n" +
                        "    \"name\": \"" + name + "\"\n" +
                        "}")
                .when()
                .patch("/api/auth/user")
                .then();

    }
    public ValidatableResponse changeUserData401(String password, String name) {
        String accessToken = "null";
        return given()
                .contentType(ContentType.JSON)
                .baseUri("https://stellarburgers.nomoreparties.site")
                .headers("Authorization", accessToken)
                .body("{\n" +
                        "    \"email\": \"AbC@yandex.ru\",\n" +
                        "    \"password\": \"" + password + "\",\n" +
                        "    \"name\": \"" + name + "\"\n" +
                        "}")
                .when()
                .patch("/api/auth/user")
                .then();
    }
}


