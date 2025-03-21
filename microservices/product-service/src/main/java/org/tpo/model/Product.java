package org.tpo.model;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.math.RoundingMode;

@Data
@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(precision = 10, scale = 2)
    @Convert(converter = BigDecimalConverter.class)
    private BigDecimal price;

    private Integer stockAmount;
}

@Converter
class BigDecimalConverter implements AttributeConverter<BigDecimal, BigDecimal> {
    @Override
    public BigDecimal convertToDatabaseColumn(BigDecimal attribute) {
        return attribute;
    }

    @Override
    public BigDecimal convertToEntityAttribute(BigDecimal dbData) {
        if (dbData == null) {
            return null;
        }
        return dbData.setScale(0, RoundingMode.HALF_UP);
    }
}
