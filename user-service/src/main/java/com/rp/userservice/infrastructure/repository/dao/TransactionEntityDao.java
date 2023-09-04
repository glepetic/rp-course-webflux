package com.rp.userservice.infrastructure.repository.dao;

import com.rp.userservice.infrastructure.dto.entity.TransactionEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface TransactionEntityDao extends ReactiveCrudRepository<TransactionEntity, Long> {

    Flux<TransactionEntity> findByUserId(long userId);

}
