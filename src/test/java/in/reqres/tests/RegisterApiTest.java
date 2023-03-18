package in.reqres.tests;

import in.reqres.models.CreateUserRequest;
import in.reqres.models.CreateUserResponse;
import in.reqres.models.RegisterUser;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static in.reqres.specs.SpecsReqres.request;
import static in.reqres.specs.SpecsReqres.responseSpec;
import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class RegisterApiTest {
    @Test
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Регистрация пользователя")
    void registerSuccessfulLombok() {
        RegisterUser body = new RegisterUser();
        body.setEmail("eve.holt@reqres.in");
        body.setPassword("pistol");

        CreateUserResponse response = given().spec(request)
                .body(body)
                .when()
                .post("/register")
                .then()
                .spec(responseSpec)
                .extract().as(CreateUserResponse.class);

        assertEquals("4", response.getId());
        assertNotNull(response.getToken());
    }
    @Test
    @Severity(SeverityLevel.BLOCKER)
    @DisplayName("Создание пользователя")
    void createUserLombok() {
        CreateUserRequest body = new CreateUserRequest();
        body.setName("Azat");
        body.setJob("student");

        CreateUserResponse response = given().spec(request)
                //.filter(customLogFilter().withCustomTemplates())
                .body(body)
                .when()
                .post("/users")
                .then()
                .statusCode(201)
                .extract().as(CreateUserResponse.class);

        assertEquals(body.getName(), response.getName());
        assertEquals(body.getJob(), response.getJob());
        assertFalse(response.getId().isEmpty());
        assertFalse(response.getCreatedAt().isEmpty());
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Обновление пользователя")
    void updateUserLombok() {
        CreateUserRequest body = new CreateUserRequest();
        body.setName("Neo");
        body.setJob("hero");

        CreateUserResponse response = given().spec(request)
                .body(body)
                .when()
                .put("/users/2")
                .then()
                .spec(responseSpec)
                .extract().as(CreateUserResponse.class);

        assertEquals(body.getName(), response.getName());
        assertEquals(body.getJob(), response.getJob());
    }
}
