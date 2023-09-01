package com.rp.productservice.domain.service;

import com.rp.productservice.domain.model.InclusiveRange;
import com.rp.productservice.domain.model.Product;
import com.rp.productservice.domain.port.ProductRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class ProductService {

    private final ValidationService validationService;
    private final ProductRepository productRepository;

    public ProductService(ValidationService validationService,
                          ProductRepository productRepository) {
        this.validationService = validationService;
        this.productRepository = productRepository;
    }

    public Mono<Product> findById(String id) {
        return this.validationService.validate(id)
                .flatMap(this.productRepository::findById);
    }

    public Flux<Product> findAll() {
        return this.productRepository.findAll();
    }

    public Flux<Product> findInRange(InclusiveRange range) {
        return this.validationService.validate(range)
                .flatMapMany(this.productRepository::findInRange);
    }

    public Mono<Product> create(Product product) {
        return this.validationService.validate(product)
                .flatMap(this.productRepository::create);
    }

    public Mono<Product> update(Product product) {
        return this.validationService.validate(product)
                .flatMap(this.productRepository::update);
    }

    public Mono<Void> deleteById(String id) {
        return this.validationService.validate(id)
                .flatMap(this.productRepository::deleteById);
    }

}
