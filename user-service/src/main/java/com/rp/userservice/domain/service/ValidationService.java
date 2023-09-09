package com.rp.userservice.domain.service;

import com.rp.userservice.domain.exception.InvalidTransactionException;
import com.rp.userservice.domain.model.UserTransaction;
import reactor.core.publisher.Mono;

import java.util.Objects;

public class ValidationService {

    public Mono<UserTransaction> validate(UserTransaction userTransaction) {
        return Mono.just(userTransaction)
                .filter(transaction -> Objects.nonNull(transaction.userId()))
                .switchIfEmpty(Mono.error(() ->
                        new InvalidTransactionException("Cannot process transaction with unspecified userId",
                                "Unspecified userId")))
                .filter(transaction -> transaction.amount() > 0)
                .switchIfEmpty(Mono.error(() ->
                        new InvalidTransactionException("Cannot process transaction with amount " + userTransaction.amount() + ". It must be greater than zero",
                                "Invalid amount value")));
    }

}
