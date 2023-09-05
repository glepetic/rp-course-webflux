package com.rp.orderservice.application.usecase;

import com.rp.orderservice.adapter.OrderAdapter;
import com.rp.orderservice.domain.service.OrderService;
import com.rp.orderservice.infrastructure.dto.request.OrderRequest;
import com.rp.orderservice.infrastructure.dto.response.OrderResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class OrderProcessor {

    private final OrderService orderService;
    private final OrderAdapter orderAdapter;

    public OrderProcessor(OrderService orderService, OrderAdapter orderAdapter) {
        this.orderService = orderService;
        this.orderAdapter = orderAdapter;
    }

    public Mono<OrderResponse> processOrder(OrderRequest orderRequest) {
        return Mono.just(orderRequest)
                .map(this.orderAdapter::toModel)
                .flatMap(this.orderService::processOrder)
                .map(this.orderAdapter::toResponse);
    }

    public Flux<OrderResponse> getOrders(long userId) {
        return this.orderService.getOrders(userId)
                .map(this.orderAdapter::toResponse);
    }

}
