package com.rp.userservice.application.usecase;

import com.rp.userservice.adapter.TransactionAdapter;
import com.rp.userservice.domain.exception.InvalidTransactionException;
import com.rp.userservice.domain.model.UserTransaction;
import com.rp.userservice.domain.service.TransactionService;
import com.rp.userservice.infrastructure.dto.request.TransactionRequest;
import com.rp.userservice.infrastructure.dto.response.TransactionResponse;
import com.rp.userservice.infrastructure.dto.response.TransactionResultResponse;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
public class TransactionProcessor {

    private final TransactionService transactionService;
    private final TransactionAdapter transactionAdapter;

    public TransactionProcessor(TransactionService transactionService,
                                TransactionAdapter transactionAdapter) {
        this.transactionService = transactionService;
        this.transactionAdapter = transactionAdapter;
    }

    public Mono<TransactionResultResponse> process(TransactionRequest transactionRequest) {
        Mono<UserTransaction> transactionMono = Mono.just(transactionRequest)
                .map(this.transactionAdapter::toModel)
                .cache();
        return transactionMono
                .flatMap(this.transactionService::process)
                .map(this.transactionAdapter::toApprovedResponse)
                .doOnError(err -> log.error("Error ocurred while processing transaction {}", transactionRequest, err))
                .onErrorResume(InvalidTransactionException.class, err -> transactionMono.map(tx -> this.transactionAdapter.toDeclinedResponse(tx, err.getReason())));
    }

    public Flux<TransactionResponse> getTransactions(long userId) {
        return this.transactionService.getTransactions(userId)
                .map(this.transactionAdapter::toResponse);
    }


}
