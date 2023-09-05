package com.rp.orderservice.domain.service;

import com.rp.orderservice.domain.model.Order;
import com.rp.orderservice.domain.model.OrderStatus;
import com.rp.orderservice.domain.model.ProductInfo;
import com.rp.orderservice.domain.model.TransactionOutcome;
import com.rp.orderservice.domain.port.OrderPort;
import com.rp.orderservice.domain.port.ProductInfoPort;
import com.rp.orderservice.domain.port.UserTransactionPort;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.function.BiFunction;

public class OrderService {

    private final ProductInfoPort productInfoPort;
    private final UserTransactionPort transactionPort;
    private final OrderPort orderPort;

    public OrderService(ProductInfoPort productInfoPort,
                        UserTransactionPort transactionPort,
                        OrderPort orderPort) {
        this.productInfoPort = productInfoPort;
        this.transactionPort = transactionPort;
        this.orderPort = orderPort;
    }

    public Mono<Order> processOrder(Order order) {

        Mono<ProductInfo> productInfoMono = this.productInfoPort.getProductInfo(order.productId())
                .cache();

        Mono<TransactionOutcome> transactionOutcomeMono = productInfoMono
                .flatMap(productInfo -> this.transactionPort.execute(order.userId(), productInfo.price()));

        BiFunction<ProductInfo, TransactionOutcome, Order> orderBuilder = (productInfo, transactionOutcome) -> Order
                .builder()
                .productId(order.productId())
                .userId(order.userId())
                .amount(productInfo.price())
                .status(transactionOutcome.success() ? OrderStatus.SUCCESS : OrderStatus.FAILURE)
                .build();

        return Mono.zip(productInfoMono, transactionOutcomeMono, orderBuilder)
                .flatMap(this.orderPort::save);

    }

    public Flux<Order> getOrders(long userId) {
        return this.orderPort.findAllByUserId(userId);
    }

}
