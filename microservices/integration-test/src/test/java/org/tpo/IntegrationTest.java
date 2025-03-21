package org.tpo;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.tpo.contoller.OrderRequest;
import org.tpo.model.Product;

import java.math.BigDecimal;
import java.time.Duration;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@Testcontainers
public class IntegrationTest {

    private static final Network network = Network.newNetwork();

    @Container
    private static final GenericContainer<?> productService = new GenericContainer<>("product-service:latest")
            .withNetwork(network)
            .withNetworkAliases("product-service")
            .withExposedPorts(8080)
            .withEnv("SPRING_DATASOURCE_URL", "jdbc:h2:mem:productdb")
            .withEnv("SPRING_DATASOURCE_DRIVER_CLASS_NAME", "org.h2.Driver")
            .withEnv("SPRING_DATASOURCE_USERNAME", "sa")
            .withEnv("SPRING_DATASOURCE_PASSWORD", "")
            .withEnv("SPRING_JPA_DATABASE_PLATFORM", "org.hibernate.dialect.H2Dialect")
            .waitingFor(Wait.forHttp("/actuator/health")
                    .forPort(8080)
                    .withStartupTimeout(Duration.ofMinutes(2)));

    @Container
    private static final GenericContainer<?> orderService = new GenericContainer<>("order-service:latest")
            .withNetwork(network)
            .withNetworkAliases("order-service")
            .withExposedPorts(8080)
            .withEnv("PRODUCT_SERVICE_URL", "http://product-service:8080")
            .withEnv("SPRING_DATASOURCE_URL", "jdbc:h2:mem:orderdb")
            .withEnv("SPRING_DATASOURCE_DRIVER_CLASS_NAME", "org.h2.Driver")
            .withEnv("SPRING_DATASOURCE_USERNAME", "sa")
            .withEnv("SPRING_DATASOURCE_PASSWORD", "")
            .withEnv("SPRING_JPA_DATABASE_PLATFORM", "org.hibernate.dialect.H2Dialect")
            .waitingFor(Wait.forHttp("/actuator/health")
                    .forPort(8080)
                    .withStartupTimeout(Duration.ofMinutes(2)));

    @BeforeAll
    static void setUp() {
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    @Test
    void testSuccessfulOrderCreation() {
        Product product = new Product();
        product.setName("Test Product");
        product.setDescription("Test Description");
        product.setPrice(new BigDecimal("100.00"));
        product.setStockAmount(10);

        String productId = given()
                .baseUri("http://" + productService.getHost())
                .port(productService.getFirstMappedPort())
                .contentType(ContentType.JSON)
                .body(product)
                .when()
                .post("/api/products")
                .then()
                .statusCode(200)
                .extract()
                .path("id");

        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setProductId(Long.valueOf(productId));
        orderRequest.setAmount(2);

        given()
                .baseUri("http://" + orderService.getHost())
                .port(orderService.getFirstMappedPort())
                .contentType(ContentType.JSON)
                .body(orderRequest)
                .when()
                .post("/api/orders")
                .then()
                .statusCode(200)
                .body("productId", equalTo(Integer.parseInt(productId)))
                .body("amount", equalTo(2))
                .body("totalPrice", equalTo(200.00f))
                .body("status", equalTo("CREATED"));

        given()
                .baseUri("http://" + productService.getHost())
                .port(productService.getFirstMappedPort())
                .when()
                .get("/api/products/" + productId)
                .then()
                .statusCode(200)
                .body("stockAmount", equalTo(8));
    }

    @Test
    void testOrderCreationWithUnavailableProduct() {
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setProductId(999L);
        orderRequest.setAmount(2);

        given()
                .baseUri("http://" + orderService.getHost())
                .port(orderService.getFirstMappedPort())
                .contentType(ContentType.JSON)
                .body(orderRequest)
                .when()
                .post("/api/orders")
                .then()
                .statusCode(500)
                .body("message", containsString("Product not found"));
    }

    @Test
    void testOrderCreationWithInsufficientStock() {
        Product product = new Product();
        product.setName("Limited Product");
        product.setDescription("Limited Stock");
        product.setPrice(new BigDecimal("100.00"));
        product.setStockAmount(1);

        String productId = given()
                .baseUri("http://" + productService.getHost())
                .port(productService.getFirstMappedPort())
                .contentType(ContentType.JSON)
                .body(product)
                .when()
                .post("/api/products")
                .then()
                .statusCode(200)
                .extract()
                .path("id");

        // создать заказ на большее количество через order-service
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setProductId(Long.valueOf(productId));
        orderRequest.setAmount(2);

        given()
                .baseUri("http://" + orderService.getHost())
                .port(orderService.getFirstMappedPort())
                .contentType(ContentType.JSON)
                .body(orderRequest)
                .when()
                .post("/api/orders")
                .then()
                .statusCode(500)
                .body("message", containsString("Insufficient stock"));
    }
}
