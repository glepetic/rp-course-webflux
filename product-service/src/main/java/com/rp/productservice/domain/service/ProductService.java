package com.rp.productservice.domain.service;

import com.rp.productservice.domain.model.InclusiveRange;
import com.rp.productservice.domain.model.Product;
import com.rp.productservice.domain.port.ProductPort;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class ProductService {

    private final ValidationService validationService;
    private final ProductPort productPort;

    public ProductService(ValidationService validationService,
                          ProductPort productPort) {
        this.validationService = validationService;
        this.productPort = productPort;
    }

    public Mono<Product> findById(String id) {
        return this.validationService.validate(id)
                .flatMap(this.productPort::findById);
    }

    public Flux<Product> findAll() {
        return this.productPort.findAll();
    }

    public Flux<Product> findInRange(InclusiveRange range) {
        return this.validationService.validate(range)
                .flatMapMany(this.productPort::findInRange);
    }

    public Mono<Product> create(Product product) {
        return this.validationService.validate(product)
                .flatMap(this.productPort::create);
    }

    public Mono<Product> update(Product product) {
        return this.validationService.validate(product)
                .flatMap(this.productPort::update);
    }

    public Mono<Void> deleteById(String id) {
        return this.validationService.validate(id)
                .flatMap(this.productPort::deleteById);
    }

}
