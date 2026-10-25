package Tests;

import Api.OrderApi;
import Api.UserApi;
import io.qameta.allure.junit4.DisplayName;
import Models.User;
import org.junit.After;
import org.junit.Test;
import Util.TestData;
import Util.Cleanup;
import java.util.List;
import static org.hamcrest.Matchers.equalTo;

public class GetUserOrdersTest {
    private final User user = TestData.generateRandomUser();
    private String accessToken;
    private final List<String> ingredients = TestData.getValidIngredients();

    @Test
    @DisplayName("Получение заказов авторизованного пользователя")
    public void testGetUserOrdersWithAuth() {
        // Создаем пользователя
        UserApi.createUser(user);
        accessToken = UserApi.getAccessToken(user);
        // Создаем заказ
        OrderApi.createOrder(ingredients, accessToken);
        // Получаем заказы пользователя
        OrderApi.getUserOrders(accessToken)
                .then()
                .statusCode(200)
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Получение заказов неавторизованного пользователя")
    public void testGetUserOrdersWithoutAuth() {
        OrderApi.getUserOrders("")
                .then()
                .statusCode(401)
                .body("message", equalTo("You should be authorised"));
    }

    @After
    public void tearDown() {
        if (accessToken != null) {
            Cleanup.deleteUser(accessToken);
        }
    }
}