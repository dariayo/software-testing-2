package org.tpo.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.tpo.logger.ProductServiceLogging;
import org.tpo.model.Product;
import org.tpo.repository.ProductRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductServiceLogging logger;

    public Product getProduct(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> {
                    logger.logProductNotFound(id);
                    return new RuntimeException("Продукт не найден");
                });
    }
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product createProduct(Product product) {
        logger.logProductCreation(product.getName(), product.getPrice().doubleValue(), product.getStockAmount());
        var savedProduct = productRepository.save(product);
        logger.logProductCreationSuccess(savedProduct.getId());
        return savedProduct;
    }

    public Product updateProduct(Long id, Product product) {
        logger.logProductUpdate(id, product.getName(), product.getPrice().doubleValue(), product.getStockAmount());

        Product existingProduct = getProduct(id);

        if (!existingProduct.getStockAmount().equals(product.getStockAmount())) {
            logger.logStockUpdate(id, existingProduct.getStockAmount(), product.getStockAmount());
        }
        if (!existingProduct.getPrice().equals(product.getPrice())) {
            logger.logPriceUpdate(id, existingProduct.getPrice().doubleValue(), product.getPrice().doubleValue());
        }

        existingProduct.setName(product.getName());
        existingProduct.setDescription(product.getDescription());
        existingProduct.setPrice(product.getPrice());
        existingProduct.setStockAmount(product.getStockAmount());

        var updatedProduct = productRepository.save(existingProduct);
        logger.logProductUpdateSuccess(updatedProduct.getId());
        return updatedProduct;
    }

    public void deleteProduct(Long id) {
        logger.logProductDeletion(id);
        productRepository.deleteById(id);
        logger.logProductDeletionSuccess(id);
    }
}
