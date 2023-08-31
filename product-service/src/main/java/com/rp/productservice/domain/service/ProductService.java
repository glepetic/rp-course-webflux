package com.rp.productservice.domain.service;

import com.rp.productservice.adapter.ProductAdapter;
import com.rp.productservice.domain.model.Range;
import com.rp.productservice.domain.port.ProductRepository;
import com.rp.productservice.infrastructure.dto.request.ProductRequest;
import com.rp.productservice.infrastructure.dto.response.ProductResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class ProductService {

    private final ValidationService validationService;
    private final ProductAdapter productAdapter;
    private final ProductRepository productRepository;

    public ProductService(ValidationService validationService,
                          ProductAdapter productAdapter,
                          ProductRepository productRepository) {
        this.validationService = validationService;
        this.productAdapter = productAdapter;
        this.productRepository = productRepository;
    }

    public Mono<ProductResponse> findById(String id) {
        return this.validationService.validate(id)
                .flatMap(this.productRepository::findById)
                .map(this.productAdapter::toResponse);
    }

    public Flux<ProductResponse> findAll() {
        return this.productRepository.findAll()
                .map(this.productAdapter::toResponse);
    }

    public Flux<ProductResponse> findInRange(int min, int max) {
        return Mono.fromSupplier(() -> new Range(min, max))
                .flatMap(this.validationService::validate)
                .flatMapMany(this.productRepository::findInRange)
                .map(this.productAdapter::toResponse);
    }

    public Mono<ProductResponse> create(ProductRequest request) {
        return Mono.just(request)
                .map(this.productAdapter::toModel)
                .flatMap(this.productRepository::create)
                .map(this.productAdapter::toResponse);
    }

    public Mono<ProductResponse> update(String id, ProductRequest request) {
        return this.validationService.validate(id)
                .map(validatedId -> this.productAdapter.toModel(validatedId, request))
                .flatMap(this.productRepository::update)
                .map(this.productAdapter::toResponse);
    }

    public Mono<Void> deleteById(String id) {
        return this.validationService.validate(id)
                .flatMap(this.productRepository::deleteById);
    }

}
