package com.rp.orderservice.domain.model;

import lombok.Builder;

@Builder
public record Order(Long id,
                    Long userId,
                    String productId,
                    Integer amount,
                    OrderStatus status,
                    String reason) {
}
