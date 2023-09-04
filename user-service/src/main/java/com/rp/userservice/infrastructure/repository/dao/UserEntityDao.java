package com.rp.userservice.infrastructure.repository.dao;

import com.rp.userservice.infrastructure.dto.entity.UserEntity;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface UserEntityDao extends ReactiveCrudRepository<UserEntity, Long> {

    @Modifying
    @Query("UPDATE users " +
            "SET balance = balance - :amount " +
            "WHERE id = :userId AND balance >= :amount"
    )
    Mono<Boolean> updateBalance(long userId, int amount);

}
