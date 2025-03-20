package org.tpo.contoller;

import lombok.Data;

@Data
public class OrderRequest {
    private Long productId;
    private Integer amount;
}
