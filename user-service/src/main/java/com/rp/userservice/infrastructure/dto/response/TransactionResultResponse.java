package com.rp.userservice.infrastructure.dto.response;

public record TransactionResultResponse(long userId,
                                        int amount,
                                        TransactionStatusResponse status,
                                        String reason) {
}
