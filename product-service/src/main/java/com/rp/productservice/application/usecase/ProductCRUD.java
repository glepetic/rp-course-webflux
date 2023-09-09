package com.rp.productservice.application.usecase;

import com.rp.productservice.adapter.ProductAdapter;
import com.rp.productservice.domain.model.InclusiveRange;
import com.rp.productservice.domain.service.ProductService;
import com.rp.productservice.infrastructure.dto.request.ProductRequest;
import com.rp.productservice.infrastructure.dto.response.ProductResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

public class ProductCRUD {

    private final ProductService productService;
    private final ProductAdapter productAdapter;
    private final Sinks.Many<ProductResponse> sink;

    public ProductCRUD(ProductService productService,
                       ProductAdapter productAdapter,
                       Sinks.Many<ProductResponse> sink) {
        this.productService = productService;
        this.productAdapter = productAdapter;
        this.sink = sink;
    }

    public Mono<ProductResponse> findById(String id) {
        return this.productService.findById(id)
                .map(this.productAdapter::toResponse);
    }

    public Flux<ProductResponse> findAll() {
        return this.productService.findAll()
                .map(this.productAdapter::toResponse);
    }

    public Flux<ProductResponse> findInRange(int min, int max) {
        return Mono.fromSupplier(() -> new InclusiveRange(min, max))
                .flatMapMany(this.productService::findInRange)
                .map(this.productAdapter::toResponse);
    }

    public Mono<ProductResponse> create(ProductRequest request) {
        return Mono.just(request)
                .map(this.productAdapter::toModel)
                .flatMap(this.productService::create)
                .map(this.productAdapter::toResponse)
                .doOnNext(this.sink::tryEmitNext);
    }

    public Mono<ProductResponse> update(String id, ProductRequest request) {
        return Mono.fromSupplier(() -> this.productAdapter.toModel(id, request))
                .flatMap(this.productService::update)
                .map(this.productAdapter::toResponse);
    }

    public Mono<Void> deleteById(String id) {
        return this.productService.deleteById(id);
    }

}
