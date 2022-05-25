import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import io.qameta.allure.junit4.DisplayName;

public class OrderListTest {

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
    }
    @Test
    @DisplayName("Check the body contents orders list")
    public void orderListTest() {
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .when()
                        .get("/api/v1/orders");
        response.then().statusCode(200)
                .assertThat().body("orders", notNullValue());
    }
}