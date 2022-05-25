import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.hamcrest.Matchers.*;
import io.qameta.allure.junit4.DisplayName;
import static io.restassured.RestAssured.given;

public class CourierLoginTest {
    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
    }
    @Before
    public void createNewCourier() {
        String login = "potter7";
        String password = "password1";
        String firstName = "harry";

        //создаем курьера
        CourierCreation courier = new CourierCreation(login, password, firstName);
        CourierClient courierClient = new CourierClient();
        Response createCourierResponse = courierClient.createCourier(courier);
        createCourierResponse.then().assertThat().statusCode(201);
    }

    @Test
    @DisplayName("Check a valid login")
    public void validLogin() {
        String login = "potter7";
        String password = "password1";
        String firstName = "harry";

        //логиним курьера и проверяем id
        CourierLogin courierLogin = new CourierLogin(login, password);
        Response loginResponse =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(courierLogin)
                        .when()
                        .post("/api/v1/courier/login");
        loginResponse.then().statusCode(200)
                .assertThat().body("id", notNullValue());
    }

    @Test
    @DisplayName("Check a login when one of the fields is missing")
    public void checkInvalidField() {
        String login = "potter7";
        String password = "password1";
        String firstName = "harry";

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
    }

    @Test
    @DisplayName("Check an invalid password")
    public void checkLoginInvalidPassword() {
        String login = "potter7";
        String password = "password1";
        String firstName = "harry";

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
    }

    @Test
    @DisplayName("Check non existent user cannot log in")
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
        response.then().statusCode(404);
    }

    @After
    public void deleteCourier(){
        String login = "potter7";
        String password = "password1";

        CourierLogin courierLogin = new CourierLogin(login,password);

        CourierID id = given()
                .header("Content-type", "application/json")
                .body(courierLogin)
                .post("/api/v1/courier/login")
                .thenReturn()
                .body()
                .as(CourierID.class);

        //Удаляем созданного курьера
        Response deleteResponse = given().delete("/api/v1/courier/" + id.getId());
    }
}
