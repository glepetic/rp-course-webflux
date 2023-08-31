package com.rp.productservice.domain.service;

import com.rp.productservice.domain.exception.InvalidIdException;
import com.rp.productservice.domain.exception.InvalidRangeException;
import com.rp.productservice.domain.model.Range;
import reactor.core.publisher.Mono;

import java.util.regex.Pattern;

public class ValidationService {

    private static final String ID_REGEX = "^[a-f\\d]{24}$";

    public Mono<String> validate(String id) {
        Pattern pattern = Pattern.compile(ID_REGEX);
        return Mono.justOrEmpty(id)
                .map(String::toLowerCase)
                .filter(pattern.asMatchPredicate())
                .switchIfEmpty(Mono.error(() -> new InvalidIdException(id)));
    }

    public Mono<Range> validate(Range range) {
        return Mono.just(range)
                .filter(r -> r.min() <= r.max())
                .switchIfEmpty(Mono.error(() -> new InvalidRangeException(range)));
    }

}
