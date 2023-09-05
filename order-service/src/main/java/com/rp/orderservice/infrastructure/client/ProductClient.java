package com.rp.orderservice.infrastructure.client;

import com.rp.orderservice.adapter.ProductAdapter;
import com.rp.orderservice.domain.model.ProductInfo;
import com.rp.orderservice.domain.port.ProductInfoPort;
import com.rp.orderservice.infrastructure.dto.integration.product.ProductDto;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public class ProductClient implements ProductInfoPort {

    private final WebClient webClient;
    private final ProductAdapter productAdapter;

    public ProductClient(WebClient webClient, ProductAdapter productAdapter) {
        this.webClient = webClient;
        this.productAdapter = productAdapter;
    }

    @Override
    public Mono<ProductInfo> getProductInfo(String productId) {
        return this.callProductService(productId)
                .map(this.productAdapter::toModel);
    }

    private Mono<ProductDto> callProductService(String productId) {
        return this.webClient.get()
                .uri("/{id}", productId)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        webClientResponse -> webClientResponse.createException()
                                .map(err -> null))
                .onStatus(HttpStatusCode::isError,
                        webClientResponse -> webClientResponse.createException()
                                .map(err -> null))
                .bodyToMono(ProductDto.class);
    }

}
