package com.rp.orderservice.infrastructure.controller;

import com.rp.orderservice.application.usecase.OrderProcessor;
import com.rp.orderservice.application.usecase.OrderQuerier;
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
    private final OrderQuerier orderQuerier;

    @Autowired
    public OrderController(OrderProcessor orderProcessor,
                           OrderQuerier orderQuerier) {
        this.orderProcessor = orderProcessor;
        this.orderQuerier = orderQuerier;
    }

    @PostMapping
    public Mono<OrderResponse> processOrder(@RequestBody Mono<OrderRequest> orderRequestMono) {
        return orderRequestMono
                .flatMap(this.orderProcessor::processOrder);
    }

    @PostMapping(value = "/all", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<OrderResponse> processAllOrders() {
        return this.orderProcessor.processAllPossibleOrders();
    }

    @GetMapping(value = "/user/{userId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<OrderResponse> getOrders(@PathVariable long userId) {
        return this.orderQuerier.getOrders(userId);
    }

}
