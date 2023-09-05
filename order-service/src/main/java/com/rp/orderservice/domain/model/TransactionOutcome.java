package com.rp.orderservice.domain.model;

public record TransactionOutcome(boolean success, String reason) {
}
