package in.reqres.tests;
import in.reqres.models.CreateUserRequest;
import in.reqres.models.CreateUserResponse;
import in.reqres.models.RegisterUser;
import in.reqres.models.Users;
import io.qameta.allure.Owner;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static in.reqres.specs.SpecsReqres.request;
import static in.reqres.specs.SpecsReqres.responseSpec;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasItem;
import static org.junit.jupiter.api.Assertions.*;


public class ReqresInTests {
    @Test
    @Owner("v.volishevskiy")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Получение списка пользователей")
    void getUserListWithLombok() {
        given()
                .spec(request)
                .when()
                .param("page", 2)
                .get("/users")
                .then()
                .log().body()
                .extract().as(Users.class);
    }

    @Test
    @Owner("v.volishevskiy")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Проверка наличия пользователя")
    void getSingleUserNotFound() {
        given()
                .spec(request)
                .when()
                .get("/users/23")
                .then()
                .statusCode(404);
    }

    @Test
    @Owner("v.volishevskiy")
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
    @Owner("v.volishevskiy")
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

    @Test
    @Owner("v.volishevskiy")
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
        assertEquals("QpwL5tke4Pnpja7X4", response.getToken());
    }

    @Test
    @Owner("v.volishevskiy")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Проверка id и email пользователя")
    void checkIdAndEmailOfFeaturedUser() {
        Users userResponse = given().spec(request)
                .when()
                .pathParam("id", "2")
                .get("/users/{id}")
                .then()
                .spec(responseSpec)
                .extract().jsonPath().getObject("data", Users.class);

        assertEquals(2, userResponse.getId());
        assertTrue(userResponse.getEmail().endsWith("@reqres.in"));
    }

    @Test
    @Owner("v.volishevskiy")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Проверка email пользователя с помощью Groovy")
    public void checkEmailUsingGroovy() {

        given()
                .spec(request)
                .when()
                .get("/users")
                .then()
                .log().body()
                .body("data.findAll{it.email =~/.*?@reqres.in/}.email.flatten()",
                        hasItem("eve.holt@reqres.in"));
    }
}
