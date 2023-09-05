package com.rp.orderservice.adapter;

import com.rp.orderservice.domain.model.TransactionOutcome;
import com.rp.orderservice.infrastructure.dto.integration.user.TransactionDto;
import com.rp.orderservice.infrastructure.dto.integration.user.TransactionResultDto;
import com.rp.orderservice.infrastructure.dto.integration.user.TransactionStatusDto;

public class TransactionAdapter {

    public TransactionDto toDto(long userId, int amount) {
        return new TransactionDto(userId, amount);
    }

    public TransactionOutcome toModel(TransactionResultDto productDto) {
        return new TransactionOutcome(TransactionStatusDto.APPROVED.equals(productDto.status()), productDto.reason());
    }
}
