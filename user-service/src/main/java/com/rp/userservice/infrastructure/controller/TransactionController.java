package com.rp.userservice.infrastructure.controller;

import com.rp.userservice.application.usecase.TransactionProcessor;
import com.rp.userservice.infrastructure.dto.request.TransactionRequest;
import com.rp.userservice.infrastructure.dto.response.TransactionResponse;
import com.rp.userservice.infrastructure.dto.response.TransactionResultResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    private final TransactionProcessor transactionProcessor;

    @Autowired
    public TransactionController(TransactionProcessor transactionProcessor) {
        this.transactionProcessor = transactionProcessor;
    }

    @PostMapping
    public Mono<TransactionResultResponse> processTransaction(@RequestBody Mono<TransactionRequest> transactionRequestMono) {
        return transactionRequestMono
                .flatMap(this.transactionProcessor::process);
    }

    @GetMapping(produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<TransactionResponse> getTransactions(@RequestParam("user_id") long userId) {
        return this.transactionProcessor.getTransactions(userId);
    }

}
