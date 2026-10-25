package Tests;

import Api.UserApi;
import io.qameta.allure.junit4.DisplayName;
import Models.User;
import org.junit.After;
import org.junit.Test;
import Util.TestData;
import Util.Cleanup;
import static org.hamcrest.Matchers.equalTo;

public class UpdateUserTest {
    private final User user = TestData.generateRandomUser();
    private String accessToken;

    @After
    public void tearDown() {
        if (accessToken != null) {
            Cleanup.deleteUser(accessToken);
        }
    }

    @Test
    @DisplayName("Изменение данных пользователя с авторизацией")
    public void testUpdateUserWithAuth() {
        UserApi.createUser(user);
        accessToken = UserApi.getAccessToken(user);
        User updatedUser = new User(user.getEmail(), "newpassword", "New Name");
        UserApi.updateUser(updatedUser, accessToken)
                .then()
                .statusCode(200)
                .body("success", equalTo(true))
                .body("user.name", equalTo(updatedUser.getName()));
    }

    @Test
    @DisplayName("Изменение данных пользователя без авторизации")
    public void testUpdateUserWithoutAuth() {
        UserApi.createUser(user);
        accessToken = UserApi.getAccessToken(user);
        User updatedUser = new User(user.getEmail(), "newpassword", "New Name");
        UserApi.updateUser(updatedUser, "")
                .then()
                .statusCode(401)
                .body("message", equalTo("You should be authorised"));
    }
}