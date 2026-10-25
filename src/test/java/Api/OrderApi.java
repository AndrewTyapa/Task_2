package Api;

import com.google.gson.Gson;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class OrderApi {
    private static final String BASE_URL = "https://stellarburgers.nomoreparties.site/api";

    public static Response createOrder(List<String> ingredients, String accessToken) {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("ingredients", ingredients);

        RequestSpecification request = given()
                .header("Content-type", "application/json")
                .body(new Gson().toJson(requestBody));

        if (accessToken != null && !accessToken.isEmpty()) {
            request.header("Authorization", accessToken);
        }

        return request.post(BASE_URL + "/orders");
    }

    public static Response getUserOrders(String accessToken) {
        return given()
                .header("Authorization", accessToken)
                .when()
                .get(BASE_URL + "/orders");
    }

    public static List<String> getAllIngredients() {
        return given()
                .when()
                .get(BASE_URL + "/ingredients")
                .then()
                .extract()
                .path("data._id");
    }
}