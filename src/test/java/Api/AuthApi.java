package Api;

import io.restassured.response.Response;
import Models.User;
import static io.restassured.RestAssured.given;

public class AuthApi {
    private static final String BASE_URL = "https://qa-stellarburgers.education-services.ru/api";

    public static Response registerUser(User user) {
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

    public static Response logoutUser(String refreshToken) {
        return given()
                .header("Content-type", "application/json")
                .body("{\"token\": \"" + refreshToken + "\"}")
                .when()
                .post(BASE_URL + "/auth/logout");
    }

    public static String getAccessToken(User user) {
        return loginUser(user)
                .then()
                .extract()
                .path("accessToken");
    }

    public static String getRefreshToken(User user) {
        return loginUser(user)
                .then()
                .extract()
                .path("refreshToken");
    }
    public static Tokens getTokens(User user) {
        Response response = loginUser(user);
        // Сразу проверяем, что сервер ответил 200. Если нет — тест падает здесь, а не дальше
        response.then().statusCode(200);
        String accessToken = response.path("accessToken");
        String refreshToken = response.path("refreshToken");
        return new Tokens(accessToken, refreshToken);
    }
}