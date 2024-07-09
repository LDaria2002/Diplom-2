package UserPack;

import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import static io.restassured.RestAssured.given;

public class CreateUserSteps {
    @Step("метод создания пользователя")
    public Response createUser (String email, String password, String name ) {
        return  given()
                .contentType(ContentType.JSON)
                .baseUri("https://stellarburgers.nomoreparties.site")
                .body("{\n" +
                        "    \"email\": \"" + email + "\",\n" +
                        "    \"password\": \"" + password + "\",\n" +
                        "    \"name\": \"" + name + "\"\n"  +
                        "}")
                .when()
                .post("/api/auth/register");
    }
}
