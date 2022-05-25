import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import java.util.ArrayList;
import io.qameta.allure.junit4.DisplayName;

public class CourierCreationTest {

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
    }

    @Test
    @DisplayName("Check create courier")
    public void createCourier(){
        //Создаем курьера
        String login = "potter7";
        String password = "password1";
        String firstName = "harry";

        CourierCreation courier = new CourierCreation(login, password, firstName);
        CourierClient courierClient = new CourierClient();
        Response createCourierResponse = courierClient.createCourier(courier);
        createCourierResponse.then().assertThat().statusCode(201);
        createCourierResponse.then().assertThat().body("ok", equalTo(true));
    }

    @Test
    @DisplayName("Check creation of an existent courier")
    public void createExistentCourier() {
        String login = "potter7";
        String password = "password1";
        String firstName = "harry";

        //Создаем курьера
        CourierCreation courier = new CourierCreation(login, password, firstName);
        CourierClient courierClient = new CourierClient();
        Response createCourierResponse = courierClient.createCourier(courier);
        createCourierResponse.then().assertThat().statusCode(201);

        //Пробуем создать курьера с такими же логином, паролем и именем
        Response sameCreateCourierResponse = courierClient.createCourier(courier);
        sameCreateCourierResponse.then().assertThat().statusCode(409);
    }

    @Test
    @DisplayName("Courier deletion")
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

//      Удаляем созданного курьера и проверяем статус
        Response response = given()
                        .delete("/api/v1/courier/" + id.getId());
        response.then().assertThat().statusCode(200);
    }

    @Test
    @DisplayName("Check if one of the mandatory fields is absent the request is not sent")
    public void createCourierFieldsValidationTest() {
        String json = "{\"name\": \"name\"}";
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
    @After public void deleteCourier(){
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
