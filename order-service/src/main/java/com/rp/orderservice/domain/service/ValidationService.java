package com.rp.orderservice.domain.service;

import com.rp.orderservice.domain.exception.InvalidOrderException;
import com.rp.orderservice.domain.model.OrderProcess;
import reactor.core.publisher.Mono;

import java.util.Objects;

public class ValidationService {

    public Mono<OrderProcess> validate(OrderProcess order) {
        return Mono.just(order)
                .filter(o -> Objects.nonNull(o.productId()) && !o.productId().isBlank())
                .switchIfEmpty(Mono.error(() -> new InvalidOrderException("The product id " + order.productId() + " provided cannot be null or blank")))
                .filter(o -> Objects.nonNull(o.userId()))
                .switchIfEmpty(Mono.error(() -> new InvalidOrderException("User id cannot have value: " + order.userId())));
    }

}
