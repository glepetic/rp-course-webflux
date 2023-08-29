package com.rp.webfluxdemo.domain.port;

import com.rp.webfluxdemo.domain.exception.MultiplicationDoesNotExistException;
import com.rp.webfluxdemo.domain.model.Multiplication;
import reactor.core.publisher.Mono;

public interface MultiplicationRepository {
    Mono<Multiplication> createMultiplication(Multiplication multiplication);
    Mono<Multiplication> getMultiplication(String id) throws MultiplicationDoesNotExistException;
}
