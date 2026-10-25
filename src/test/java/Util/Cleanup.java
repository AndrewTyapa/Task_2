package Util;

import static io.restassured.RestAssured.given;

public class Cleanup {
    public static void deleteUser(String accessToken) {
        try {
                     given()
                    .header("Authorization", accessToken)
                    .when()
                    .delete("https://stellarburgers.nomoreparties.site/api/auth/user")
                    .then()
                    .statusCode(202);
        } catch (Exception e) {
            System.out.println("Failed to delete user: " + e.getMessage());
        }
    }
}