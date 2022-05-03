import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

import java.util.ArrayList;

public class CourierCreationTest {

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
    }

    @Test
    public void createCourier() {
        String login = "potter7";
        String password = "password1";
        String firstName = "harry";

        CourierCreation courier = new CourierCreation(login, password, firstName);
        Response response = given()
                .header("Content-type", "application/json")
                .and()
                .body(courier)
                .when()
                .post("/api/v1/courier");
        response.then().assertThat().body("ok", equalTo(true))
                .statusCode(201);

        CourierLogin courierLogin = new CourierLogin(login,password);

        CourierID id = given()
            .header("Content-type", "application/json")
            .body(courierLogin)
            .post("/api/v1/courier/login")
            .thenReturn()
            .body()
            .as(CourierID.class);

        Response deleteResponse = given().delete("/api/v1/courier/" + id.getId());
    }

    @Test
    public void createExistentCourier() {
        String login = "potter9";
        String password = "password1";
        String firstName = "harry";

        CourierCreation courier = new CourierCreation(login, password, firstName);
        Response response = given()
                .header("Content-type", "application/json")
                .and()
                .body(courier)
                .when()
                .post("/api/v1/courier");
        response.then()
                .statusCode(201);

        Response sameCourierResponse = given()
                .header("Content-type", "application/json")
                .and()
                .body(courier)
                .when()
                .post("/api/v1/courier");
        sameCourierResponse.then().assertThat()
                .statusCode(409);

        CourierLogin courierLogin = new CourierLogin(login, password);

        CourierID id = given()
                .header("Content-type", "application/json")
                .body(courierLogin)
                .post("/api/v1/courier/login")
                .thenReturn()
                .body()
                .as(CourierID.class);

        Response deleteResponse = given().delete("/api/v1/courier/" + id.getId());
        deleteResponse.then().assertThat().statusCode(200);
    }

    @Test
    public void deleteCourierTest() {
        ScooterRegisterCourier courier = new ScooterRegisterCourier();
        ArrayList<String> loginAndPassword =  courier.registerNewCourierAndReturnLoginPassword();
        String login = loginAndPassword.get(0);
        String password = loginAndPassword.get(1);
        CourierLogin courierLoginAndPassword = new CourierLogin(login, password);

        CourierID id = given()
                .header("Content-type", "application/json")
                .body(courierLoginAndPassword)
                .post("/api/v1/courier/login")
                .thenReturn()
                .body()
                .as(CourierID.class);

        Response response = given()
                        .delete("/api/v1/courier/" + id.getId());
        response.then().assertThat().statusCode(200);
    }

    @Test
    public void createCourierFieldsValidationTest() {
        String json = "{\"name\": \"name\"}";;
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(json)
                        .when()
                        .post("/api/v1/courier");
        response.then().assertThat()
                .statusCode(400);
    }
}
