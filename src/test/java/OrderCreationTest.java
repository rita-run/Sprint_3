import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

public class OrderCreationTest {

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
    }
    private String[] colorBlack = new String[] {"black"};
    private String[] colorGrey = new String[] {"grey"};
    private String[] colorBoth = new String[] {"black", "grey"};
    private String[] colorNone = new String[] {};

    @Test
    public void createOrderBlack() {
        OrderCreation order = new OrderCreation("Naruto", "potter", "Konoha, 142 apt.", "4", "+7 800 355 35 35", 2, "2020-06-06", "comment", colorBlack);
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(order)
                        .when()
                        .post("/api/v1/orders");
        response.then().assertThat().body("track", notNullValue())
                .statusCode(201);
    }

    @Test
    public void createOrderGrey() {
    OrderCreation order = new OrderCreation("Naruto", "potter", "Konoha, 142 apt.", "4", "+7 800 355 35 35", 2, "2020-06-06", "comment", colorGrey);
    Response response =
            given()
                    .header("Content-type", "application/json")
                    .and()
                    .body(order)
                    .when()
                    .post("/api/v1/orders");
        response.then().assertThat().body("track", notNullValue())
                .statusCode(201);
    }

    @Test
    public void createOrderBothColors() {
        OrderCreation order = new OrderCreation("Naruto", "potter", "Konoha, 142 apt.", "4", "+7 800 355 35 35", 2, "2020-06-06", "comment", colorBoth);
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(order)
                        .when()
                        .post("/api/v1/orders");
        response.then().assertThat().body("track", notNullValue())
                .statusCode(201);
    }

    @Test
    public void createOrderColorNone() {
        OrderCreation order = new OrderCreation("Naruto", "potter", "Konoha, 142 apt.", "4", "+7 800 355 35 35", 2, "2020-06-06", "comment", colorNone);
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(order)
                        .when()
                        .post("/api/v1/orders");
        response.then().assertThat().body("track", notNullValue())
                .statusCode(201);
    }
}