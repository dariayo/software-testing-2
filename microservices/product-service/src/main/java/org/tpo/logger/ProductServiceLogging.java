package org.tpo.logger;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ProductServiceLogging {
    public void logProductCreation(String name, Double price, Integer amount) {
        log.info("Создание продукта: name={}, price={}, amount={}", name, price, amount);
    }

    public void logProductCreationSuccess(Long productId) {
        log.info("Продукт успешно создан: productId={}", productId);
    }

    public void logProductUpdate(Long productId, String name, Double price, Integer amount) {
        log.info("Обновление продукта: productId={}, name={}, price={}, amount={}",
                productId, name, price, amount);
    }

    public void logProductUpdateSuccess(Long productId) {
        log.info("Продукт успешно обновлен: productId={}", productId);
    }

    public void logProductDeletion(Long productId) {
        log.info("Удаление продукта: productId={}", productId);
    }

    public void logProductDeletionSuccess(Long productId) {
        log.info("Продукт успешно удален: productId={}", productId);
    }

    public void logProductNotFound(Long productId) {
        log.warn("Продукт не найден: productId={}", productId);
    }

    public void logStockUpdate(Long productId, Integer oldStock, Integer newAmount) {
        log.info("Обновление количества на складе: productId={}, oldStock={}, newAmount={}",
                productId, oldStock, newAmount);
    }

    public void logPriceUpdate(Long productId, Double oldPrice, Double newPrice) {
        log.info("Обновление цены: productId={}, oldPrice={}, newPrice={}",
                productId, oldPrice, newPrice);
    }
}
