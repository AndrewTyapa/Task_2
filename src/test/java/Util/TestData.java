package Util;

import Api.OrderApi;
import Models.User;
import java.util.List;
import java.util.UUID;

public class TestData {
    public static User generateRandomUser() {
        String email = "testuser_" + UUID.randomUUID().toString().substring(0, 8) + "@example.com";
        String password = "P@ssw0rd_" + UUID.randomUUID().toString().substring(0, 4);
        String name = "Test User " + UUID.randomUUID().toString().substring(0, 4);

        return new User(email, password, name);
    }

    public static List<String> getValidIngredients() {
        return OrderApi.getAllIngredients();
    }

    public static List<String> getInvalidIngredients() {
        return List.of("invalidIngredient1", "invalidIngredient2");
    }
}