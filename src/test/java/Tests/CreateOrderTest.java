package Tests;

import Api.AuthApi;
import Api.OrderApi;
import Api.Tokens;
import Api.UserApi;
import io.qameta.allure.junit4.DisplayName;
import Models.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import Util.TestData;
import Util.Cleanup;
import java.util.List;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;

public class CreateOrderTest {
    private final User user = TestData.generateRandomUser();
    private String accessToken;
    private final List<String> ingredients = TestData.getValidIngredients();
    private List<String> validIngredients;


    @Before
    public void setup() {
        UserApi.createUser(user);
        Tokens tokens = AuthApi.getTokens(user);
        accessToken = tokens.getAccessToken();
    }


    @Test
    @DisplayName("Создание заказа с авторизацией")
    public void testCreateOrderWithAuth() {
        OrderApi.createOrder(ingredients, accessToken)
                .then()
                .statusCode(200)
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Создание заказа без авторизации")
    public void testCreateOrderWithoutAuth() {
        createOrderWithoutAuth();
    }
    public void createOrderWithoutAuth() {
        OrderApi.createOrder(ingredients, "")
                .then()
                .statusCode(200)
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Создание заказа с ингредиентами")
    public void testCreateOrderWithIngredients() {
        List<String> ingredientIds = TestData.getValidIngredients();
        createOrderWithProvidedIngredients(ingredientIds);
    }
    public void createOrderWithProvidedIngredients(List<String> ingredientIds) {
        OrderApi.createOrder(ingredientIds, "")
                .then()
                .statusCode(200)
                .body("success", equalTo(true))
                .body("name", notNullValue())
                .body("order.number", notNullValue());
    }

    @Test
    @DisplayName("Создание заказа без ингредиентов")
    public void testCreateOrderWithoutIngredients() {
        createOrderWithEmptyIngredients();
    }
    public void createOrderWithEmptyIngredients() {
        OrderApi.createOrder(List.of(), "")
                .then()
                .statusCode(400)
                .body("success", equalTo(false))
                .body("message", equalTo("Ingredient ids must be provided"));
    }

    @Test
    @DisplayName("Создание заказа с неверным хешем ингредиентов")
    public void testCreateOrderWithInvalidIngredients() {
        createOrderWithInvalidIngredients();
    }
    public void createOrderWithInvalidIngredients() {
        OrderApi.createOrder(TestData.getInvalidIngredients(), "")
                .then()
                .statusCode(500);
    }

    @After
    public void tearDown() {
        if (accessToken != null) {
            Cleanup.deleteUser(accessToken);
        }
    }
}