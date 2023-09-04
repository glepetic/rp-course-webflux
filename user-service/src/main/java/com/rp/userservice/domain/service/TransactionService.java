package com.rp.userservice.domain.service;

import com.rp.userservice.domain.exception.InvalidTransactionException;
import com.rp.userservice.domain.exception.UserNotFoundException;
import com.rp.userservice.domain.model.UserTransaction;
import com.rp.userservice.domain.port.UserRepository;
import com.rp.userservice.domain.port.UserTransactionRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class TransactionService {

    private final ValidationService validationService;
    private final UserTransactionRepository transactionRepository;
    private final UserRepository userRepository;

    public TransactionService(ValidationService validationService,
                              UserTransactionRepository transactionRepository,
                              UserRepository userRepository) {
        this.validationService = validationService;
        this.transactionRepository = transactionRepository;
        this.userRepository = userRepository;
    }

    public Mono<UserTransaction> process(UserTransaction transaction) {
        return Mono.just(transaction)
                .flatMap(this.validationService::validate)
                .filterWhen(tx -> this.userRepository.exists(tx.userId()))
                .switchIfEmpty(Mono.error(() ->
                        new InvalidTransactionException("User " + transaction.userId() + " does not exist", "User does not exist")))
                .filterWhen(tx -> this.userRepository.updateBalance(tx.userId(), tx.amount()))
                .switchIfEmpty(Mono.error(() ->
                        new InvalidTransactionException("Insufficient balance of user " + transaction.userId() + " for transaction with amount " + transaction.amount(), "Insufficient balance")))
                .flatMap(this.transactionRepository::save);
    }

    public Flux<UserTransaction> getTransactions(long userId) {
        return Mono.just(userId)
                .filterWhen(this.userRepository::exists)
                .switchIfEmpty(Mono.error(() -> new UserNotFoundException(userId)))
                .flatMapMany(this.transactionRepository::findByUserId);
    }

}
