package com.rp.userservice.adapter;

import com.rp.userservice.domain.model.UserTransaction;
import com.rp.userservice.infrastructure.dto.entity.TransactionEntity;
import com.rp.userservice.infrastructure.dto.request.TransactionRequest;
import com.rp.userservice.infrastructure.dto.response.TransactionResultResponse;
import com.rp.userservice.infrastructure.dto.response.TransactionStatusResponse;

import java.time.LocalDateTime;

public class TransactionAdapter {

    public UserTransaction toModel(TransactionRequest request) {
        return new UserTransaction(request.userId(), request.amount());
    }

    public TransactionResultResponse toApprovedResponse(UserTransaction transaction) {
        return this.toResultResponse(transaction, TransactionStatusResponse.APPROVED);
    }

    public TransactionResultResponse toDeclinedResponse(UserTransaction transaction) {
        return this.toResultResponse(transaction, TransactionStatusResponse.DECLINED);
    }

    private TransactionResultResponse toResultResponse(UserTransaction transaction, TransactionStatusResponse status) {
        return new TransactionResultResponse(transaction.userId(), transaction.amount(), status);
    }

    public TransactionEntity toEntity(UserTransaction transaction) {
        return TransactionEntity.builder()
                .userId(transaction.userId())
                .amount(transaction.amount())
                .dateTime(LocalDateTime.now())
                .build();
    }

    public UserTransaction toModel(TransactionEntity entity) {
        return new UserTransaction(entity.userId(), entity.amount());
    }

}
