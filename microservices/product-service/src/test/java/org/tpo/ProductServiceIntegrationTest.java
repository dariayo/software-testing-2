package org.tpo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.tpo.model.Product;
import org.tpo.repository.ProductRepository;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@ActiveProfiles("test")
public class ProductServiceIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15")
            .withDatabaseName("productdb")
            .withUsername("test")
            .withPassword("test");

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ProductRepository productRepository;

    @DynamicPropertySource
    static void postgresProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.datasource.driver-class-name", () -> "org.postgresql.Driver");
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
        registry.add("spring.jpa.properties.hibernate.dialect", () -> "org.hibernate.dialect.PostgreSQLDialect");
        registry.add("spring.sql.init.mode", () -> "never");
    }

    @BeforeEach
    void setUp() {
        productRepository.deleteAll();
    }

    @Test
    void testCreateProduct() {
        Product product = new Product();
        product.setName("Test Product");
        product.setPrice(new BigDecimal("100"));
        product.setStockAmount(10);

        ResponseEntity<Product> response = restTemplate.postForEntity("/api/products", product, Product.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getId());
        assertEquals("Test Product", response.getBody().getName());
        assertEquals(new BigDecimal("100"), response.getBody().getPrice());
        assertEquals(10, response.getBody().getStockAmount());

        Product savedProduct = productRepository.findById(response.getBody().getId()).orElse(null);
        assertNotNull(savedProduct);
        assertEquals(response.getBody().getId(), savedProduct.getId());
        assertEquals(response.getBody().getName(), savedProduct.getName());
        assertEquals(response.getBody().getPrice(), savedProduct.getPrice());
        assertEquals(response.getBody().getStockAmount(), savedProduct.getStockAmount());
    }

    @Test
    void testGetProduct() {
        Product product = new Product();
        product.setName("Test Product");
        product.setPrice(new BigDecimal("100"));
        product.setStockAmount(10);
        Product savedProduct = productRepository.save(product);

        ResponseEntity<Product> response = restTemplate.getForEntity("/api/products/{id}", Product.class, savedProduct.getId());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(savedProduct.getId(), response.getBody().getId());
        assertEquals(savedProduct.getName(), response.getBody().getName());
        assertEquals(savedProduct.getPrice(), response.getBody().getPrice());
        assertEquals(savedProduct.getStockAmount(), response.getBody().getStockAmount());
    }

    @Test
    void testGetAllProducts() {
        Product product1 = new Product();
        product1.setName("Product 1");
        product1.setPrice(new BigDecimal("100"));
        product1.setStockAmount(10);
        productRepository.save(product1);

        Product product2 = new Product();
        product2.setName("Product 2");
        product2.setPrice(new BigDecimal("200"));
        product2.setStockAmount(5);
        productRepository.save(product2);

        ResponseEntity<List> response = restTemplate.getForEntity("/api/products", List.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
    }

    @Test
    void testUpdateProduct() {
        Product product = new Product();
        product.setName("Test Product");
        product.setPrice(new BigDecimal("100"));
        product.setStockAmount(10);
        Product savedProduct = productRepository.save(product);

        savedProduct.setPrice(new BigDecimal("150"));
        savedProduct.setStockAmount(15);

        ResponseEntity<Product> response = restTemplate.postForEntity("/api/products", savedProduct, Product.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(savedProduct.getId(), response.getBody().getId());
        assertEquals(new BigDecimal("150"), response.getBody().getPrice());
        assertEquals(15, response.getBody().getStockAmount());

        Product updatedProduct = productRepository.findById(savedProduct.getId()).orElse(null);
        assertNotNull(updatedProduct);
        assertEquals(new BigDecimal("150"), updatedProduct.getPrice());
        assertEquals(15, updatedProduct.getStockAmount());
    }
}
