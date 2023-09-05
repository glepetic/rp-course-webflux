package com.rp.userservice.infrastructure.repository;

import com.rp.userservice.adapter.TransactionAdapter;
import com.rp.userservice.domain.model.UserTransaction;
import com.rp.userservice.domain.port.UserTransactionPort;
import com.rp.userservice.infrastructure.repository.dao.TransactionEntityDao;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class TransactionRDBRepository implements UserTransactionPort {

    private final TransactionEntityDao transactionDao;
    private final TransactionAdapter transactionAdapter;

    public TransactionRDBRepository(TransactionEntityDao transactionDao,
                                    TransactionAdapter transactionAdapter) {
        this.transactionDao = transactionDao;
        this.transactionAdapter = transactionAdapter;
    }

    @Override
    public Flux<UserTransaction> findByUserId(long userId) {
        return this.transactionDao.findByUserId(userId)
                .map(this.transactionAdapter::toModel);
    }

    @Override
    public Mono<UserTransaction> save(UserTransaction transaction) {
        return Mono.just(transaction)
                .map(this.transactionAdapter::toEntity)
                .flatMap(this.transactionDao::save)
                .map(this.transactionAdapter::toModel);
    }

}
