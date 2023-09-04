package com.rp.productservice.infrastructure.boot;

import com.rp.productservice.domain.model.Product;
import com.rp.productservice.domain.service.ProductService;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import reactor.core.publisher.Flux;

@Slf4j
public class DBSetup {

    private final ProductService productService;

    public DBSetup(ProductService productService) {
        this.productService = productService;
    }

    @PostConstruct
    public void setUp() {
        this.basicProducts()
                .flatMap(this.productService::create)
                .subscribe(product -> log.info("Created: {}", product));
    }

    private Flux<Product> basicProducts() {
        return Flux.just(
                new Product(ObjectId.get().toString(), "4k-tv", 1000),
                new Product(ObjectId.get().toString(), "slr-camera", 750),
                new Product(ObjectId.get().toString(), "iphone", 800),
                new Product(ObjectId.get().toString(), "headphone", 100)
        );
    }

}
