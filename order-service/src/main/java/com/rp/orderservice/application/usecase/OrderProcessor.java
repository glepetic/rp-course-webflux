package com.rp.orderservice.application.usecase;

import com.rp.orderservice.adapter.OrderAdapter;
import com.rp.orderservice.domain.model.OrderProcess;
import com.rp.orderservice.domain.service.OrderService;
import com.rp.orderservice.infrastructure.dto.request.OrderRequest;
import com.rp.orderservice.infrastructure.dto.response.OrderResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class OrderProcessor {

    private final OrderService orderService;
    private final OrderAdapter orderAdapter;

    public OrderProcessor(OrderService orderService,
                          OrderAdapter orderAdapter) {
        this.orderService = orderService;
        this.orderAdapter = orderAdapter;
    }

    public Mono<OrderResponse> processOrder(OrderRequest orderRequest) {
        return Mono.just(orderRequest)
                .map(this.orderAdapter::toModel)
                .flatMap(this::processOrder);

    }

    private Mono<OrderResponse> processOrder(OrderProcess orderProcess) {
        return Mono.just(orderProcess)
                .flatMap(this.orderService::processOrder)
                .map(this.orderAdapter::toResponse);
    }

    public Flux<OrderResponse> processAllPossibleOrders() {
        return this.orderService.findAllPossibleOrders()
                .flatMap(this::processOrder);
    }

}
