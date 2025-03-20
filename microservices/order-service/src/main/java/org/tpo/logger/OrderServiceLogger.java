package org.tpo.logger;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class OrderServiceLogger {
    public void logOrderCreation(Long productId, Integer quantity) {
        log.info("Создание заказа: productId={}, quantity={}", productId, quantity);
    }

    public void logOrderCreationSuccess(Long orderId) {
        log.info("Заказ успешно создан: orderId={}", orderId);
    }

    public void logOrderCreationError(Long productId, String error) {
        log.error("Ошибка при создании заказа: productId={}, error={}", productId, error);
    }

    public void logInsufficientStock(Long productId, Integer requested, Integer available) {
        log.warn("Недостаточно товара на складе: productId={}, requested={}, available={}",
                productId, requested, available);
    }
}
