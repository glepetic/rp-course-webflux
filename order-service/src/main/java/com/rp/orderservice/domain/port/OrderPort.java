package com.rp.orderservice.domain.port;

import com.rp.orderservice.domain.model.Order;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface OrderPort {
    Mono<Order> save(Order order);
    Flux<Order> findAllByUserId(long userId);
}
