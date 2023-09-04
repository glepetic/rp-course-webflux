package com.rp.userservice.infrastructure.dto.response;

public record TransactionResponse(long userId,
                                  int amount,
                                  String date) {
}
