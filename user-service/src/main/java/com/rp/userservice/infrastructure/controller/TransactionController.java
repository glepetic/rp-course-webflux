package com.rp.userservice.infrastructure.controller;

import com.rp.userservice.application.usecase.TransactionProcessing;
import com.rp.userservice.application.usecase.UserCRUD;
import com.rp.userservice.infrastructure.dto.request.TransactionRequest;
import com.rp.userservice.infrastructure.dto.request.UserRequest;
import com.rp.userservice.infrastructure.dto.response.TransactionResultResponse;
import com.rp.userservice.infrastructure.dto.response.UserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    private final TransactionProcessing transactionProcessing;

    @Autowired
    public TransactionController(TransactionProcessing transactionProcessing) {
        this.transactionProcessing = transactionProcessing;
    }

    @PostMapping
    public Mono<TransactionResultResponse> processTransaction(@RequestBody Mono<TransactionRequest> transactionRequestMono) {
        return transactionRequestMono
                .flatMap(this.transactionProcessing::process);
    }

}
