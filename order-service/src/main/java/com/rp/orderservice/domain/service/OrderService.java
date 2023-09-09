package com.rp.orderservice.domain.service;

import com.rp.orderservice.domain.exception.OrderProcessingException;
import com.rp.orderservice.domain.mapper.OrderMapper;
import com.rp.orderservice.domain.model.Order;
import com.rp.orderservice.domain.model.OrderProcess;
import com.rp.orderservice.domain.port.OrderPort;
import com.rp.orderservice.domain.port.ProductInfoPort;
import com.rp.orderservice.domain.port.UserPort;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
public class OrderService {

    private final String key = this.getClass().getSimpleName() + " ->";

    private final ValidationService validationService;
    private final ProductInfoPort productInfoPort;
    private final UserPort userPort;
    private final OrderPort orderPort;
    private final OrderMapper orderMapper;

    public OrderService(ValidationService validationService,
                        ProductInfoPort productInfoPort,
                        UserPort userPort,
                        OrderPort orderPort,
                        OrderMapper orderMapper) {
        this.validationService = validationService;
        this.productInfoPort = productInfoPort;
        this.userPort = userPort;
        this.orderPort = orderPort;
        this.orderMapper = orderMapper;
    }

    public Flux<Order> getOrders(long userId) {
        return this.orderPort.findAllByUserId(userId);
    }

    public Flux<OrderProcess> findAllPossibleOrders() {
        return Flux.zip(this.productInfoPort.getAllProductIds(), this.userPort.getAllUserIds(),
                (productId, userId) -> OrderProcess.builder()
                        .productId(productId)
                        .userId(userId)
                        .build()
        );
    }

    public Mono<Order> processOrder(OrderProcess orderProcess) {
        return this.validationService.validate(orderProcess)
                .flatMap(this::getProductInfo)
                .flatMap(this::executeTransaction)
                .map(this.orderMapper::buildOrder)
                .onErrorResume(OrderProcessingException.class, err -> this.handleError(orderProcess, err))
                .flatMap(this.orderPort::save)
                .doOnError(err -> log.error("{} unexpected error encountered: {}", key, err.getMessage(), err));
    }

    private Mono<OrderProcess> getProductInfo(OrderProcess orderProcess) {
        return this.productInfoPort.getProductInfo(orderProcess.productId())
                .map(productInfo -> orderProcess
                        .toBuilder()
                        .productInfo(productInfo)
                        .build()
                );
    }

    private Mono<OrderProcess> executeTransaction(OrderProcess orderProcess) {
        return this.userPort.executeTransaction(orderProcess.userId(), orderProcess.productInfo().price())
                .map(transactionOutcome -> orderProcess
                        .toBuilder()
                        .transactionOutcome(transactionOutcome)
                        .build()
                );
    }

    private Mono<Order> handleError(OrderProcess orderProcess, OrderProcessingException err) {
        log.error("{} failed to process order. Detail -> {}", key, err.getFullDetail(), err);
        return Mono.fromSupplier(() -> this.orderMapper.buildOrderFromError(orderProcess, err));
    }

}
