package com.rp.productservice.infrastructure.controller;

import com.rp.productservice.infrastructure.dto.response.ProductResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@CrossOrigin
@RestController
@RequestMapping("/products/stream")
public class ProductStreamController {

    private final Flux<ProductResponse> flux;

    @Autowired
    public ProductStreamController(Flux<ProductResponse> flux) {
        this.flux = flux;
    }

    @GetMapping(value = "/{maxPrice}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ProductResponse> getProductUpdates(@PathVariable Integer maxPrice) {
        return this.flux.filter(p -> p.price() <= maxPrice);
    }


}
