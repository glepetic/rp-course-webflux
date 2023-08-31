package com.rp.productservice.domain.service;

import com.rp.productservice.domain.exception.InvalidIdException;
import com.rp.productservice.domain.exception.InvalidProductDetailException;
import com.rp.productservice.domain.exception.InvalidProductPriceException;
import com.rp.productservice.domain.exception.InvalidRangeException;
import com.rp.productservice.domain.model.InclusiveRange;
import com.rp.productservice.infrastructure.dto.request.ProductRequest;
import reactor.core.publisher.Mono;

import java.util.Objects;
import java.util.regex.Pattern;

public class ValidationService {

    private static final String ID_REGEX = "^[a-f\\d]{24}$";
    private static final int MINIMUM_PRICE = 1;

    public Mono<String> validate(String id) {
        Pattern pattern = Pattern.compile(ID_REGEX);
        return Mono.justOrEmpty(id)
                .map(String::toLowerCase)
                .filter(pattern.asMatchPredicate())
                .switchIfEmpty(Mono.error(() -> new InvalidIdException(id)));
    }

    public Mono<ProductRequest> validate(ProductRequest productRequest) {
        return Mono.just(productRequest)
                .filter(request -> Objects.nonNull(request.price())  && request.price() >= MINIMUM_PRICE)
                .switchIfEmpty(Mono.error(() -> new InvalidProductPriceException(productRequest.price())))
                .filter(request -> Objects.nonNull(request.detail()) && !request.detail().isBlank())
                .switchIfEmpty(Mono.error(() -> new InvalidProductDetailException(productRequest.detail())));
    }
    public Mono<InclusiveRange> validate(InclusiveRange range) {
        return Mono.just(range)
                .filter(r -> r.min() <= r.max())
                .switchIfEmpty(Mono.error(() -> new InvalidRangeException(range)));
    }

}
