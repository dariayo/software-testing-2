package org.tpo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.tpo.model.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
