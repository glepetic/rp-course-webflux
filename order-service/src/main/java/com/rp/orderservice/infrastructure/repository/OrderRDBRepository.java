package com.rp.orderservice.infrastructure.repository;

import com.rp.orderservice.adapter.OrderAdapter;
import com.rp.orderservice.domain.model.Order;
import com.rp.orderservice.domain.port.OrderPort;
import com.rp.orderservice.infrastructure.repository.dao.OrderEntityDao;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

public class OrderRDBRepository implements OrderPort {

    private final OrderEntityDao orderDao;
    private final OrderAdapter orderAdapter;

    public OrderRDBRepository(OrderEntityDao orderDao, OrderAdapter orderAdapter) {
        this.orderDao = orderDao;
        this.orderAdapter = orderAdapter;
    }

    @Override
    public Mono<Order> save(Order order) {
        return Mono.fromSupplier(() -> this.orderAdapter.toEntity(order))
                .publishOn(Schedulers.boundedElastic())
                .map(this.orderDao::save) // blocking
                .map(this.orderAdapter::toModel);
    }

    @Override
    public Flux<Order> findAllByUserId(long userId) {
        return Mono.just(userId)
                .publishOn(Schedulers.boundedElastic())
                .flatMapIterable(this.orderDao::findAllByUserId) // blocking
                .map(this.orderAdapter::toModel);
    }

}
