package com.rp.orderservice.infrastructure.dto.integration.user;

public record TransactionResultDto(long userId,
                                   int amount,
                                   TransactionStatusDto status,
                                   String reason) {
}
