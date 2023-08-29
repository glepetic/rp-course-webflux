package com.rp.webfluxdemo.application.service;

import com.rp.webfluxdemo.domain.exception.InvalidRangeException;
import com.rp.webfluxdemo.domain.model.Range;
import reactor.core.publisher.Mono;

public class ValidationService {
    public Mono<Range> validateRange(Range range) {
        return Mono.just(range)
                .filter(rng -> rng.from() <= rng.to())
                .switchIfEmpty(Mono.error(() -> new InvalidRangeException(range.from(), range.to())));
    }

}
