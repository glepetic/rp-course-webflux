package com.rp.userservice.adapter;

import com.rp.userservice.domain.model.UserTransaction;
import com.rp.userservice.infrastructure.dto.entity.TransactionEntity;
import com.rp.userservice.infrastructure.dto.request.TransactionRequest;
import com.rp.userservice.infrastructure.dto.response.TransactionResponse;
import com.rp.userservice.infrastructure.dto.response.TransactionResultResponse;
import com.rp.userservice.infrastructure.dto.response.TransactionStatusResponse;

import java.time.LocalDateTime;

public class TransactionAdapter {

    public TransactionResponse toResponse(UserTransaction transaction) {
        return new TransactionResponse(transaction.userId(), transaction.amount(), transaction.datetime().toLocalDate().toString());
    }

    public UserTransaction toModel(TransactionRequest request) {
        return new UserTransaction(request.userId(), request.amount(), LocalDateTime.now());
    }

    public TransactionResultResponse toApprovedResponse(UserTransaction transaction) {
        return this.toResultResponse(transaction, TransactionStatusResponse.APPROVED, null);
    }

    public TransactionResultResponse toDeclinedResponse(UserTransaction transaction, String reason) {
        return this.toResultResponse(transaction, TransactionStatusResponse.DECLINED, reason);
    }

    private TransactionResultResponse toResultResponse(UserTransaction transaction, TransactionStatusResponse status, String reason) {
        return new TransactionResultResponse(transaction.userId(), transaction.amount(), status, reason);
    }

    public TransactionEntity toEntity(UserTransaction transaction) {
        return TransactionEntity.builder()
                .userId(transaction.userId())
                .amount(transaction.amount())
                .dateTime(transaction.datetime())
                .build();
    }

    public UserTransaction toModel(TransactionEntity entity) {
        return new UserTransaction(entity.userId(), entity.amount(), entity.dateTime());
    }

}
