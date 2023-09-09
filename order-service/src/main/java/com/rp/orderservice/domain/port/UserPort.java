package com.rp.orderservice.domain.port;

import com.rp.orderservice.domain.model.TransactionOutcome;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserPort {
    Mono<TransactionOutcome> executeTransaction(long userId, int amount);
    Flux<Long> getAllUserIds();
}
