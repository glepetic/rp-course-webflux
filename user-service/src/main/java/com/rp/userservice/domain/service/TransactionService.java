package com.rp.userservice.domain.service;

import com.rp.userservice.domain.exception.InvalidTransactionException;
import com.rp.userservice.domain.exception.UserNotFoundException;
import com.rp.userservice.domain.model.UserTransaction;
import com.rp.userservice.domain.port.UserPort;
import com.rp.userservice.domain.port.UserTransactionPort;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class TransactionService {

    private final ValidationService validationService;
    private final UserTransactionPort transactionPort;
    private final UserPort userPort;

    public TransactionService(ValidationService validationService,
                              UserTransactionPort transactionPort,
                              UserPort userPort) {
        this.validationService = validationService;
        this.transactionPort = transactionPort;
        this.userPort = userPort;
    }

    public Mono<UserTransaction> process(UserTransaction transaction) {
        return Mono.just(transaction)
                .flatMap(this.validationService::validate)
                .filterWhen(tx -> this.userPort.exists(tx.userId()))
                .switchIfEmpty(Mono.error(() ->
                        new InvalidTransactionException("User " + transaction.userId() + " does not exist", "User does not exist")))
                .filterWhen(tx -> this.userPort.updateBalance(tx.userId(), tx.amount()))
                .switchIfEmpty(Mono.error(() ->
                        new InvalidTransactionException("Insufficient balance of user " + transaction.userId() + " for transaction with amount " + transaction.amount(), "Insufficient balance")))
                .flatMap(this.transactionPort::save);
    }

    public Flux<UserTransaction> getTransactions(long userId) {
        return Mono.just(userId)
                .filterWhen(this.userPort::exists)
                .switchIfEmpty(Mono.error(() -> new UserNotFoundException(userId)))
                .flatMapMany(this.transactionPort::findByUserId);
    }

}
