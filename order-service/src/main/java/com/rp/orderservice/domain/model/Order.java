package com.rp.orderservice.domain.model;

import lombok.Builder;

@Builder
public record Order(long id,
                    long userId,
                    String productId,
                    int amount,
                    OrderStatus status) {
}
