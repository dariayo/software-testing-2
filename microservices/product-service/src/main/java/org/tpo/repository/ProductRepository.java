package org.tpo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.tpo.model.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
