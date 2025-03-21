package org.tpo;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.tpo.client.ProductServiceClient;
import org.tpo.contoller.OrderController;
import org.tpo.model.Order;
import org.tpo.model.Product;
import org.tpo.service.OrderService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderController.class)
public class OrderServiceTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @MockBean
    private ProductServiceClient productServiceClient;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testCreateOrder() throws Exception {
        Product product = new Product();
        product.setId(1L);
        product.setName("Test Product");
        product.setPrice(new BigDecimal("100"));
        product.setStockAmount(10);

        Order order = new Order();
        order.setId(1L);
        order.setProductId(1L);
        order.setAmount(2);
        order.setTotalPrice(new BigDecimal("200"));
        order.setOrderDate(LocalDateTime.now());
        order.setStatus("CREATED");

        when(productServiceClient.getProduct(1L)).thenReturn(product);
        when(orderService.createOrder(any(), any())).thenReturn(order);
        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(order)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.productId").value(1))
                .andExpect(jsonPath("$.amount").value(2))
                .andExpect(jsonPath("$.totalPrice").value(200))
                .andExpect(jsonPath("$.status").value("CREATED"));

    }

    @Test
    void testProductServiceFailure() throws Exception {
        Order order = new Order();
        order.setProductId(1L);
        order.setAmount(2);

        when(productServiceClient.getProduct(1L)).thenThrow(new RuntimeException("Product service is unavailable"));
        when(orderService.createOrder(any(), any())).thenThrow(new RuntimeException("Product service is unavailable"));

        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(order)))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.status").value(500))
                .andExpect(jsonPath("$.message").value("Product service is unavailable"));

    }

    @Test
    void testSmallAmountOfProducts() throws Exception {
        Product product = new Product();
        product.setId(1L);
        product.setName("Test Product");
        product.setPrice(new BigDecimal("100"));
        product.setStockAmount(5);

        Order order = new Order();
        order.setProductId(1L);
        order.setAmount(10);

        when(productServiceClient.getProduct(1L)).thenReturn(product);
        when(orderService.createOrder(any(), any())).thenThrow(new RuntimeException("Недостаточно товара на складе"));

        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(order)))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.status").value(500))
                .andExpect(jsonPath("$.message").value("Недостаточно товара на складе"));
    }

    @Test
    public void testHandleServiceDelay() throws Exception {
        Product product = new Product();
        product.setId(1L);
        product.setName("Test Product");
        product.setPrice(new BigDecimal("100"));
        product.setStockAmount(10);

        Order order = new Order();
        order.setId(1L);
        order.setProductId(1L);
        order.setAmount(2);
        order.setTotalPrice(new BigDecimal("200"));
        order.setOrderDate(LocalDateTime.now());
        order.setStatus("CREATED");

        CompletableFuture<Product> delayedProduct = CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(2);
                return product;
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        when(productServiceClient.getProduct(1L)).thenReturn(delayedProduct.get());
        when(orderService.createOrder(any(), any())).thenReturn(order);

        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(order)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.productId").value(1))
                .andExpect(jsonPath("$.amount").value(2))
                .andExpect(jsonPath("$.totalPrice").value(200))
                .andExpect(jsonPath("$.status").value("CREATED"));
    }
}
