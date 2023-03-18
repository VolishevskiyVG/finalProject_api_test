package in.reqres.tests;

import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static in.reqres.specs.SpecsReqres.request;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasItem;

public class UserApiTest {
    @Test
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Проверка получения списка пользователей")
    void getUserListWithLombok() {
        given()
                .spec(request)
                .when()
                .get("/users?page=2")
                .then()
                .log().body()
                .body("data.findAll{it.id}.last_name.flatten()",hasItem("Howell"));
    }
    @Test
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
}
