package com.rp.userservice.domain.port;

import com.rp.userservice.domain.model.UserTransaction;
import reactor.core.publisher.Mono;

public interface UserTransactionRepository {
    Mono<UserTransaction> save(UserTransaction transaction);
}
