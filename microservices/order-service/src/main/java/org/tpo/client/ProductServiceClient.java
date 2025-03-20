package org.tpo.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.tpo.model.Product;

@FeignClient(name = "order-product-client", url = "http://localhost:8081")
public interface ProductServiceClient {
    @GetMapping("/api/products/{id}")
    Product getProduct(@PathVariable("id") Long id);
}
