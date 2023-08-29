package com.rp.webfluxdemo.infrastructure.repository;

import com.rp.webfluxdemo.adapter.driven.MultiplicationAdapter;
import com.rp.webfluxdemo.domain.exception.MultiplicationDoesNotExistException;
import com.rp.webfluxdemo.domain.model.Multiplication;
import com.rp.webfluxdemo.domain.port.MultiplicationRepository;
import com.rp.webfluxdemo.infrastructure.dto.entity.MultiplicationEntity;
import com.rp.webfluxdemo.infrastructure.repository.dao.Dao;
import reactor.core.publisher.Mono;

public class InMemoryMultiplicationRepository implements MultiplicationRepository {

    private final Dao<MultiplicationEntity> dao;
    private final MultiplicationAdapter adapter;

    public InMemoryMultiplicationRepository(Dao<MultiplicationEntity> dao,
                                            MultiplicationAdapter adapter) {
        this.dao = dao;
        this.adapter = adapter;
    }

    @Override
    public Mono<Multiplication> createMultiplication(Multiplication multiplication) {
        return Mono.just(multiplication)
                .map(this.adapter::to)
                .flatMap(this.dao::insert)
                .map(this.adapter::to);
    }

    @Override
    public Mono<Multiplication> getMultiplication(String id) throws MultiplicationDoesNotExistException {
        return this.dao.get(id)
                .map(this.adapter::to)
                .switchIfEmpty(Mono.error(() -> new MultiplicationDoesNotExistException(id)));
    }

}
