package com.rp.orderservice.adapter;

import com.rp.orderservice.domain.model.Order;
import com.rp.orderservice.domain.model.OrderProcess;
import com.rp.orderservice.domain.model.OrderStatus;
import com.rp.orderservice.infrastructure.dto.entity.OrderEntity;
import com.rp.orderservice.infrastructure.dto.entity.OrderEntityStatus;
import com.rp.orderservice.infrastructure.dto.request.OrderRequest;
import com.rp.orderservice.infrastructure.dto.response.OrderResponse;
import com.rp.orderservice.infrastructure.dto.response.OrderStatusResponse;

public class OrderAdapter {

    public OrderProcess toModel(OrderRequest orderRequest) {
        return OrderProcess.builder()
                .productId(orderRequest.productId())
                .userId(orderRequest.userId())
                .build();
    }

    public OrderResponse toResponse(Order order) {
        return new OrderResponse(
                order.id(),
                order.userId(),
                order.productId(),
                order.amount(),
                this.toStatusResponse(order.status()),
                order.reason()
        );
    }

    private OrderStatusResponse toStatusResponse(OrderStatus status) {
        return switch (status) {
            case SUCCESS -> OrderStatusResponse.OK;
            case FAILURE -> OrderStatusResponse.KO;
        };
    }

    public OrderEntity toEntity(Order order) {
        return OrderEntity
                .builder()
                .userId(order.userId())
                .productId(order.productId())
                .amount(order.amount())
                .status(this.toEntityStatus(order.status()))
                .reason(order.reason())
                .build();
    }

    public OrderEntityStatus toEntityStatus(OrderStatus status) {
        return switch (status) {
            case SUCCESS -> OrderEntityStatus.COMPLETED;
            case FAILURE -> OrderEntityStatus.FAILED;
        };
    }

    public Order toModel(OrderEntity entity) {
        return new Order(entity.getId(),
                entity.getUserId(),
                entity.getProductId(),
                entity.getAmount(),
                this.toStatusModel(entity.getStatus()),
                entity.getReason()
        );
    }

    public OrderStatus toStatusModel(OrderEntityStatus status) {
        return switch (status) {
            case COMPLETED -> OrderStatus.SUCCESS;
            case FAILED -> OrderStatus.FAILURE;
        };
    }

}
