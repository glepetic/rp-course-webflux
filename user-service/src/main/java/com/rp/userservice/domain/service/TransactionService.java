package com.rp.userservice.domain.service;

import com.rp.userservice.domain.model.UserTransaction;
import com.rp.userservice.domain.port.UserRepository;
import com.rp.userservice.domain.port.UserTransactionRepository;
import reactor.core.publisher.Mono;

public class TransactionService {

    private ValidationService validationService;
    private final UserTransactionRepository transactionRepository;
    private final UserRepository userRepository;

    public TransactionService(UserTransactionRepository transactionRepository,
                              UserRepository userRepository) {
        this.transactionRepository = transactionRepository;
        this.userRepository = userRepository;
    }

    public Mono<UserTransaction> process(UserTransaction transaction) {
        return Mono.just(transaction)
                .filterWhen(tx -> this.userRepository.updateBalance(tx.userId(), tx.amount()))
                .flatMap(this.transactionRepository::save);
    }

}
