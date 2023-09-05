package com.rp.orderservice.infrastructure.controller;

import com.rp.orderservice.application.usecase.OrderProcessor;
import com.rp.orderservice.infrastructure.dto.request.OrderRequest;
import com.rp.orderservice.infrastructure.dto.response.OrderResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderProcessor orderProcessor;

    @Autowired
    public OrderController(OrderProcessor orderProcessor) {
        this.orderProcessor = orderProcessor;
    }

    @PostMapping
    public Mono<OrderResponse> processOrder(@RequestBody Mono<OrderRequest> orderRequestMono) {
        return orderRequestMono
                .flatMap(this.orderProcessor::processOrder);
    }

    @GetMapping(value = "/user/{userId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<OrderResponse> getOrders(@PathVariable long userId) {
        return this.orderProcessor.getOrders(userId);
    }

}
