package com.rp.orderservice.application.usecase;

import com.rp.orderservice.adapter.OrderAdapter;
import com.rp.orderservice.domain.service.OrderService;
import com.rp.orderservice.infrastructure.dto.response.OrderResponse;
import reactor.core.publisher.Flux;

public class OrderQuerier {

    private final OrderService orderService;
    private final OrderAdapter orderAdapter;

    public OrderQuerier(OrderService orderService, OrderAdapter orderAdapter) {
        this.orderService = orderService;
        this.orderAdapter = orderAdapter;
    }

    public Flux<OrderResponse> getOrders(long userId) {
        return this.orderService.getOrders(userId)
                .map(this.orderAdapter::toResponse);
    }
}
