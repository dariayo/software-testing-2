package org.tpo.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Product {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer stockAmount;

}
