package com.rp.orderservice.infrastructure.dto.response;

public record OrderResponse(long orderId,
                            long userId,
                            String productId,
                            int amount,
                            OrderStatusResponse status) {
}
