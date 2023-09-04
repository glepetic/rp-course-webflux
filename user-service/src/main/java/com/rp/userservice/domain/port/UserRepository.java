package com.rp.userservice.domain.port;

import com.rp.userservice.domain.model.User;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserRepository {
    Flux<User> findAll();
    Mono<User> findById(long id);
    Mono<User> save(User user);
    Mono<User> update(User user);
    Mono<Void> deleteById(long id);
    Mono<Boolean> updateBalance(long userId, int amount);
    Mono<Boolean> exists(long userId);
}
