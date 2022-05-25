import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import static org.hamcrest.Matchers.notNullValue;
import io.qameta.allure.junit4.DisplayName;

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
    @DisplayName("Check create order Black")
    public void createOrderBlack() {
        //Создаем заказ
        OrderCreation order = new OrderCreation("Naruto", "potter", "Konoha, 142 apt.", "4", "+7 800 355 35 35", 2, "2020-06-06", "comment", colorBlack);
        OrdersClient ordersClient = new OrdersClient();
        Response createOrderResponse = ordersClient.createOrder(order);
        //Проверяем код ответа и поле track
        createOrderResponse.then().assertThat().statusCode(201);
        createOrderResponse.then().assertThat().body("track", notNullValue());
    }

    @Test
    @DisplayName("Check create order Grey")
    public void createOrderGrey() {
        OrderCreation order = new OrderCreation("Naruto", "potter", "Konoha, 142 apt.", "4", "+7 800 355 35 35", 2, "2020-06-06", "comment", colorGrey);
        OrdersClient ordersClient = new OrdersClient();
        Response createOrderResponse = ordersClient.createOrder(order);
        //Проверяем код ответа и поле track
        createOrderResponse.then().assertThat().statusCode(201);
        createOrderResponse.then().assertThat().body("track", notNullValue());
    }

    @Test
    @DisplayName("Check create order both colors")
    public void createOrderBothColors() {
        OrderCreation order = new OrderCreation("Naruto", "potter", "Konoha, 142 apt.", "4", "+7 800 355 35 35", 2, "2020-06-06", "comment", colorBoth);
        OrdersClient ordersClient = new OrdersClient();
        Response createOrderResponse = ordersClient.createOrder(order);
        //Проверяем код ответа и поле track
        createOrderResponse.then().assertThat().statusCode(201);
        createOrderResponse.then().assertThat().body("track", notNullValue());
    }

    @Test
    @DisplayName("Check create order none colors")
    public void createOrderColorNone() {
        OrderCreation order = new OrderCreation("Naruto", "potter", "Konoha, 142 apt.", "4", "+7 800 355 35 35", 2, "2020-06-06", "comment", colorNone);
        OrdersClient ordersClient = new OrdersClient();
        Response createOrderResponse = ordersClient.createOrder(order);
        //Проверяем код ответа и поле track
        createOrderResponse.then().assertThat().statusCode(201);
        createOrderResponse.then().assertThat().body("track", notNullValue());
    }
}