package com.rp.orderservice.domain.mapper;

import com.rp.orderservice.domain.exception.OrderProcessingException;
import com.rp.orderservice.domain.model.Order;
import com.rp.orderservice.domain.model.OrderProcess;
import com.rp.orderservice.domain.model.OrderStatus;

public class OrderMapper {

    public Order buildOrder(OrderProcess orderProcess) {
        return Order.builder().productId(orderProcess.productId())
                .userId(orderProcess.userId())
                .amount(orderProcess.productInfo().price())
                .status(orderProcess.transactionOutcome().success() ? OrderStatus.SUCCESS : OrderStatus.FAILURE)
                .reason(orderProcess.transactionOutcome().reason())
                .build();
    }

    public Order buildOrderFromError(OrderProcess orderProcess, OrderProcessingException err) {
        return Order.builder()
                .productId(orderProcess.productId())
                .userId(orderProcess.userId())
                .status(OrderStatus.FAILURE)
                .reason(err.getReason())
                .build();
    }

}
