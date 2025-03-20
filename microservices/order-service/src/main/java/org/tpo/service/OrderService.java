package org.tpo.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.tpo.client.ProductServiceClient;
import org.tpo.logger.OrderServiceLogger;
import org.tpo.model.Order;
import org.tpo.repository.OrderRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final ProductServiceClient productServiceClient;
    private final OrderServiceLogger logger;

    @Transactional
    public Order createOrder(Long productId, Integer amount) {
        logger.logOrderCreation(productId, amount);
        try {
            var product = productServiceClient.getProduct(productId);
            if (product.getStockAmount() < productId) {
                logger.logInsufficientStock(productId, amount, product.getStockAmount());
                throw new RuntimeException("Недостаточно товара на складе");
            }

            var order = new Order();
            order.setProductId(productId);
            order.setAmount(amount);
            order.setTotalPrice(product.getPrice().multiply(BigDecimal.valueOf(amount)));
            order.setOrderDate(LocalDateTime.now());
            order.setStatus("CREATED");
            logger.logOrderCreationSuccess(order.getId());
            return orderRepository.save(order);
        } catch (RuntimeException e) {
            logger.logOrderCreationError(productId, e.getMessage());
            throw e;
        }
    }

    public Order getOrder(Long id) {
        return orderRepository.findById(id).orElseThrow(() -> new RuntimeException("Заказ не найден"));
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }
}
