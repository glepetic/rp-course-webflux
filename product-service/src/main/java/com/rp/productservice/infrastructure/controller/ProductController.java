package com.rp.productservice.infrastructure.controller;

import com.rp.productservice.application.usecase.ProductCRUD;
import com.rp.productservice.infrastructure.dto.request.ProductRequest;
import com.rp.productservice.infrastructure.dto.response.ProductResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductCRUD productCRUD;

    @Autowired
    public ProductController(ProductCRUD productCRUD) {
        this.productCRUD = productCRUD;
    }

    @GetMapping(produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ProductResponse> getProducts(@RequestParam int minPrice,
                                             @RequestParam int maxPrice) {
        return this.productCRUD.findInRange(minPrice, maxPrice);
    }

    @GetMapping(value = "/all", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ProductResponse> getProducts() {
        return this.productCRUD.findAll();
    }

    @GetMapping("/{id}")
    public Mono<ProductResponse> getProduct(@PathVariable String id) {
        return this.productCRUD.findById(id);
    }

    @PostMapping
    public Mono<ResponseEntity<ProductResponse>> createProduct(@RequestBody Mono<ProductRequest> productRequestMono) {
        return productRequestMono
                .flatMap(this.productCRUD::create)
                .map(productResponse -> ResponseEntity
                        .status(HttpStatus.CREATED)
                        .body(productResponse)
                );
    }

    @PutMapping("/{id}")
    public Mono<ProductResponse> updateProduct(@PathVariable String id,
                                               @RequestBody Mono<ProductRequest> productRequestMono) {
        return productRequestMono
                .flatMap(productRequest -> this.productCRUD.update(id, productRequest));
    }

    @DeleteMapping("/{id}")
    public Mono<Void> deleteProduct(@PathVariable String id) {
        return this.productCRUD.deleteById(id);
    }

}
