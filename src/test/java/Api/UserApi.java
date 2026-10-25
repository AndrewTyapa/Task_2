package Api;

import io.restassured.response.Response;
import Models.User;
import static io.restassured.RestAssured.given;

public class UserApi {
    private static final String BASE_URL = "https://qa-stellarburgers.education-services.ru/api";

    public static Response createUser(User user) {
        return given()
                .header("Content-type", "application/json")
                .body(user)
                .when()
                .post(BASE_URL + "/auth/register");
    }

    public static Response loginUser(User user) {
        return given()
                .header("Content-type", "application/json")
                .body(user)
                .when()
                .post(BASE_URL + "/auth/login");
    }

    public static Response updateUser(User user, String accessToken) {
        return given()
                .header("Content-type", "application/json")
                .header("Authorization", accessToken)
                .body(user)
                .when()
                .patch(BASE_URL + "/auth/user");
    }

    public static String getAccessToken(User user) {
        return loginUser(user)
                .then()
                .extract()
                .path("accessToken");
    }
}