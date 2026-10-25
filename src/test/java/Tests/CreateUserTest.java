package Tests;

import Api.UserApi;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import Models.User;
import org.junit.After;
import org.junit.Test;
import Util.TestData;
import Util.Cleanup;
import static org.hamcrest.Matchers.equalTo;

public class CreateUserTest {
    private final User user = TestData.generateRandomUser();
    private String accessToken;

    @Test
    @DisplayName("Создание уникального пользователя")
    public void testCreateUniqueUser() {
        Response response = UserApi.createUser(user);
        response.then()
                .statusCode(200)
                .body("success", equalTo(true));
        accessToken = response.path("accessToken");
    }

    @Test
    @DisplayName("Создание уже зарегистрированного пользователя")
    public void testCreateExistingUser() {
        UserApi.createUser(user);
        accessToken = UserApi.getAccessToken(user);
        UserApi.createUser(user)
                .then()
                .statusCode(403)
                .body("message", equalTo("User already exists"));
    }

    @Test
    @DisplayName("Создание пользователя без обязательного поля")
    public void testCreateUserWithoutRequiredField() {
        User invalidUser = new User("", "password", "name");
        UserApi.createUser(invalidUser)
                .then()
                .statusCode(403)
                .body("message", equalTo("Email, password and name are required fields"));
    }

    @After
    public void tearDown() {
        if (accessToken != null) {
            Cleanup.deleteUser(accessToken);
        }
    }
}