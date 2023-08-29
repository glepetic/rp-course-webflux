package com.rp.webfluxdemo.infrastructure.service;

import com.rp.webfluxdemo.adapter.driver.ResultAdapter;
import com.rp.webfluxdemo.application.usecase.SquareCalculator;
import com.rp.webfluxdemo.application.usecase.MultiplicationCalculator;
import com.rp.webfluxdemo.domain.model.Multiplication;
import com.rp.webfluxdemo.domain.port.MultiplicationRepository;
import com.rp.webfluxdemo.infrastructure.dto.request.MultiplicationRequest;
import com.rp.webfluxdemo.infrastructure.dto.response.ResultResponse;
import com.rp.webfluxdemo.infrastructure.dto.response.SimpleIdResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class MathService {

    private final SquareCalculator squareCalculator;
    private final MultiplicationCalculator multiplicationCalculator;
    private final MultiplicationRepository multiplicationRepository;
    private final ResultAdapter resultAdapter;

    public MathService(SquareCalculator squareCalculator,
                       MultiplicationCalculator multiplicationCalculator,
                       MultiplicationRepository multiplicationRepository,
                       ResultAdapter resultAdapter) {
        this.squareCalculator = squareCalculator;
        this.multiplicationCalculator = multiplicationCalculator;
        this.multiplicationRepository = multiplicationRepository;
        this.resultAdapter = resultAdapter;
    }

    public Mono<ResultResponse> getSquare(long number) {
        return this.squareCalculator.calculateSquare(number)
                .map(this.resultAdapter::to);
    }

    public Flux<ResultResponse> getMultiplicationTable(Long number, Integer from, Integer to) {
        return this.multiplicationCalculator.calculateTable(number, from, to)
                .map(this.resultAdapter::to);
    }

    public Mono<SimpleIdResponse> createMultiplication(MultiplicationRequest multiplicationRequest) {
        return Mono.just(multiplicationRequest)
                .map(req -> Multiplication.builder()
                        .numbers(new long[]{req.first(), req.second()})
                        .build())
                .flatMap(this.multiplicationRepository::createMultiplication)
                .map(multiplication -> new SimpleIdResponse(multiplication.id()));
    }

    public Mono<ResultResponse> getMultiplicationResult(String id) {
        return this.multiplicationRepository.getMultiplication(id)
                .flatMap(this.multiplicationCalculator::multiply)
                .map(this.resultAdapter::to);
    }
}
