package com.rp.orderservice.infrastructure.dto.response;

public record OrderResponse(Long orderId,
                            Long userId,
                            String productId,
                            Integer amount,
                            OrderStatusResponse status,
                            String reason) {
}
