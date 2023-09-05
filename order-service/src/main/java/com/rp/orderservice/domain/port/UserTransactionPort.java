package com.rp.orderservice.domain.port;

import com.rp.orderservice.domain.model.TransactionOutcome;
import reactor.core.publisher.Mono;

public interface UserTransactionPort {
    Mono<TransactionOutcome> execute(long userId, int amount);
}
