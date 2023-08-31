package com.rp.productservice.domain.port;

import com.rp.productservice.domain.model.Product;
import com.rp.productservice.domain.model.InclusiveRange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductRepository {
    Mono<Product> findById(String id);
    Flux<Product> findAll();
    Flux<Product> findInRange(InclusiveRange range);
    Mono<Product> create(Product product);
    Mono<Product> update(Product product);
    Mono<Void> deleteById(String id);
}
