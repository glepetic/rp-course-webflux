package com.rp.userservice.application.usecase;

import com.rp.userservice.adapter.TransactionAdapter;
import com.rp.userservice.domain.model.UserTransaction;
import com.rp.userservice.domain.service.TransactionService;
import com.rp.userservice.infrastructure.dto.request.TransactionRequest;
import com.rp.userservice.infrastructure.dto.response.TransactionResultResponse;
import reactor.core.publisher.Mono;

public class TransactionProcessing {

    private final TransactionService transactionService;
    private final TransactionAdapter transactionAdapter;

    public TransactionProcessing(TransactionService transactionService,
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
                .switchIfEmpty(Mono.defer(() -> transactionMono.map(this.transactionAdapter::toDeclinedResponse)));
    }

}
