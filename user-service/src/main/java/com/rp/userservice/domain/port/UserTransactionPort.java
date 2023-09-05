package com.rp.userservice.domain.port;

import com.rp.userservice.domain.model.UserTransaction;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserTransactionPort {
    Flux<UserTransaction> findByUserId(long userId);
    Mono<UserTransaction> save(UserTransaction transaction);
}
