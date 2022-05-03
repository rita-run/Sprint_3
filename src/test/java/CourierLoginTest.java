import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import static org.hamcrest.Matchers.*;

import static io.restassured.RestAssured.given;

public class CourierLoginTest {
    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
    }

    @Test
    public void createCourier() {
        String login = "potter8";
        String password = "password1";
        String firstName = "harry";

        //создаем курьера
        CourierCreation courier = new CourierCreation(login, password, firstName);
        Response response = given()
                .header("Content-type", "application/json")
                .and()
                .body(courier)
                .when()
                .post("/api/v1/courier");
        response.then()
                .statusCode(201);

        //логиним курьера и проверяем id
        CourierLogin courierLogin = new CourierLogin(login, password);
        Response loginResponse =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(courierLogin)
                        .when()
                        .post("/api/v1/courier/login");
        loginResponse.then().assertThat().body("id", notNullValue())
                .statusCode(200);

        //получаем id
        CourierID id = given()
                .header("Content-type", "application/json")
                .body(courierLogin)
                .post("/api/v1/courier/login")
                .thenReturn()
                .body()
                .as(CourierID.class);

        //удаляем курьера
        Response deleteResponse = given().delete("/api/v1/courier/" + id.getId());
        deleteResponse.then().assertThat().statusCode(200);
    }

    @Test
    public void checkInvalidField() {
        String login = "potter7";
        String password = "password1";
        String firstName = "harry";

        //создаем курьера
        CourierCreation courier = new CourierCreation(login, password, firstName);
        Response response = given()
                .header("Content-type", "application/json")
                .and()
                .body(courier)
                .when()
                .post("/api/v1/courier");
        response.then()
                .statusCode(201);

        CourierLogin courierInvalidLogin = new CourierLogin(login, null);
        Response invalidFieldResponse =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(courierInvalidLogin)
                        .when()
                        .post("/api/v1/courier/login");
        invalidFieldResponse.then().assertThat()
                .statusCode(504);

        //логиним курьера и проверяем id
        CourierLogin courierLogin = new CourierLogin(login, password);
        Response loginResponse =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(courierLogin)
                        .when()
                        .post("/api/v1/courier/login");
        loginResponse.then().assertThat().body("id", notNullValue())
                .statusCode(200);

        //получаем id
        CourierID id = given()
                .header("Content-type", "application/json")
                .body(courierLogin)
                .post("/api/v1/courier/login")
                .thenReturn()
                .body()
                .as(CourierID.class);

        //удаляем курьера
        Response deleteResponse = given().delete("/api/v1/courier/" + id.getId());
        deleteResponse.then().assertThat().statusCode(200);
    }

    @Test
    public void checkLoginInvalidPassword() {
        String login = "potter7";
        String password = "password1";
        String firstName = "harry";

        //создаем курьера
        CourierCreation courier = new CourierCreation(login, password, firstName);
        Response response = given()
                .header("Content-type", "application/json")
                .and()
                .body(courier)
                .when()
                .post("/api/v1/courier");
        response.then()
                .statusCode(201);

        //пробуем залогиниться с неправильным паролем
        CourierLogin courierInvalidLogin = new CourierLogin(login, "123");
        Response invalidFieldResponse =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(courierInvalidLogin)
                        .when()
                        .post("/api/v1/courier/login");
        invalidFieldResponse.then().assertThat()
                .statusCode(404);

        //логиним курьера и проверяем id
        CourierLogin courierLogin = new CourierLogin(login, password);
        Response loginResponse =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(courierLogin)
                        .when()
                        .post("/api/v1/courier/login");
        loginResponse.then().assertThat().body("id", notNullValue())
                .statusCode(200);

        //получаем id
        CourierID id = given()
                .header("Content-type", "application/json")
                .body(courierLogin)
                .post("/api/v1/courier/login")
                .thenReturn()
                .body()
                .as(CourierID.class);

        //удаляем курьера
        Response deleteResponse = given().delete("/api/v1/courier/" + id.getId());
        deleteResponse.then().assertThat().statusCode(200);
    }

    @Test
    public void checkLoginNegativeNotExistentUser() {
        String login = "voldemort";
        String password = "password1";

        CourierLogin courierLogin = new CourierLogin(login, password);
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(courierLogin)
                        .when()
                        .post("/api/v1/courier/login");
        response.then().assertThat()//.body("ok", equalTo("true"))
                .statusCode(404);
    }
}
