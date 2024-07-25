package userPack;

import data.User;
import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static data.Uri.BASE_URI;
import static data.Uri.CREATE_USER;
import static io.restassured.RestAssured.given;

public class CreateUserSteps {
    @Step("метод создания пользователя")
    public Response createUser (User user) {
        return  given()
                .contentType(ContentType.JSON)
                .baseUri(BASE_URI)
                .body(user)
                .when()
                .post(CREATE_USER);
    }
}
