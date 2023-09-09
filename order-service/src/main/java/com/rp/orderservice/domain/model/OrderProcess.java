package com.rp.orderservice.domain.model;

import lombok.Builder;

@Builder(toBuilder = true)
public record OrderProcess(String productId,
                           Long userId,
                           ProductInfo productInfo,
                           TransactionOutcome transactionOutcome) {
}
