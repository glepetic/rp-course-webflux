package com.rp.productservice.infrastructure.boot;

import com.rp.productservice.application.usecase.ProductCRUD;
import com.rp.productservice.infrastructure.dto.request.ProductRequest;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
public class DBSetup {

    private final ProductCRUD productCRUD;

    public DBSetup(ProductCRUD productCRUD) {
        this.productCRUD = productCRUD;
    }

    @PostConstruct
    public void setUp() {
        this.basicProducts()
                .concatWith(this.constantInsertionProducts())
                .flatMap(this.productCRUD::create)
                .subscribe(product -> log.info("Created: {}", product));
    }

    private Flux<ProductRequest> basicProducts() {
        return Flux.just(
                new ProductRequest("4k-tv", 1000),
                new ProductRequest("slr-camera", 750),
                new ProductRequest("iphone", 800),
                new ProductRequest("headphone", 100)
        );
    }

    private Flux<ProductRequest> constantInsertionProducts() {
        return Flux.range(1, 1000)
                .map(i -> new ProductRequest("product-" + i, ThreadLocalRandom.current().nextInt(100, 1000)))
                .delayElements(Duration.ofSeconds(2));
    }


}
