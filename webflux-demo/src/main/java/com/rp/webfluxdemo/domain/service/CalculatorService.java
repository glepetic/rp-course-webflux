package com.rp.webfluxdemo.domain.service;

import com.rp.webfluxdemo.domain.model.Multiplication;
import com.rp.webfluxdemo.domain.model.Range;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Arrays;

public class CalculatorService {

    private final String key = this.getClass().getSimpleName() + " -> ";


    public Mono<Long> calculatePower(long number, int exponent) {
        return Mono.fromSupplier(() -> Math.pow(number, exponent))
                .map(Double::longValue);
    }

    public Flux<Long> calculateMultiplicationTable(Long number, Range range) {
        return Mono.just(range)
                .flatMapMany(rng -> Flux.range(rng.from(), rng.to() - rng.from() + 1))
                .delayElements(Duration.ofSeconds(1))
                .doOnNext(i -> System.out.printf("%s processing multiplication of %s and %s%n", key, number, i))
                .map(i -> i * number);
    }

    public Mono<Long> multiply(Multiplication multiplication) {
        return Mono.just(multiplication)
                .map(Multiplication::numbers)
                .flatMapMany(numbers -> Flux.fromStream(() -> Arrays.stream(numbers).boxed()))
                .reduce(1L, (num1, num2) -> num1 * num2);
    }

}
