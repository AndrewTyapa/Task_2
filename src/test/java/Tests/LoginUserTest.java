package Tests;

import Api.UserApi;
import io.qameta.allure.junit4.DisplayName;
import Models.User;
import org.junit.After;
import org.junit.Test;
import Util.TestData;
import Util.Cleanup;
import static org.hamcrest.Matchers.equalTo;

public class LoginUserTest {
    private final User user = TestData.generateRandomUser();
    private String accessToken;

    @After
    public void tearDown() {
        if (accessToken != null) {
            Cleanup.deleteUser(accessToken);
        }
    }

    @Test
    @DisplayName("Логин под существующим пользователем")
    public void testLoginWithValidCredentials() {
        UserApi.createUser(user);
        accessToken = UserApi.getAccessToken(user);
        UserApi.loginUser(user)
                .then()
                .statusCode(200)
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Логин с неверными данными")
    public void testLoginWithInvalidCredentials() {
        User invalidUser = new User("invalid@ya.com", "password", "Invalid User");
        UserApi.loginUser(invalidUser)
                .then()
                .statusCode(401)
                .body("message", equalTo("email or password are incorrect"));
    }
}