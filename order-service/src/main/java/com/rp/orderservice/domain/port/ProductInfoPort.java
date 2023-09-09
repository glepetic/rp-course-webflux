package com.rp.orderservice.domain.port;

import com.rp.orderservice.domain.model.ProductInfo;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductInfoPort {
    Mono<ProductInfo> getProductInfo(String productId);
    Flux<String> getAllProductIds();
}
