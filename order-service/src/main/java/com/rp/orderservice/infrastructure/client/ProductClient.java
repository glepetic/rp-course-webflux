package com.rp.orderservice.infrastructure.client;

import com.rp.orderservice.adapter.ProductAdapter;
import com.rp.orderservice.domain.exception.OrderProcessingException;
import com.rp.orderservice.domain.model.ProductInfo;
import com.rp.orderservice.domain.port.ProductInfoPort;
import com.rp.orderservice.infrastructure.dto.integration.product.ProductDto;
import com.rp.orderservice.util.ExceptionUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.Exceptions;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;
import reactor.util.retry.RetryBackoffSpec;

import java.time.Duration;
import java.util.function.Function;

@Slf4j
public class ProductClient implements ProductInfoPort {

    private final int timeoutSeconds;
    private final int retryAttempts;
    private final int retryFixedDelaySeconds;
    private final WebClient webClient;
    private final ProductAdapter productAdapter;
    private final ExceptionUtil exceptionUtil;

    public ProductClient(int timeoutSeconds,
                         int retryAttempts,
                         int retryFixedDelaySeconds,
                         WebClient webClient,
                         ProductAdapter productAdapter,
                         ExceptionUtil exceptionUtil) {
        this.timeoutSeconds = timeoutSeconds;
        this.retryAttempts = retryAttempts;
        this.retryFixedDelaySeconds = retryFixedDelaySeconds;
        this.webClient = webClient;
        this.productAdapter = productAdapter;
        this.exceptionUtil = exceptionUtil;
    }

    @Override
    public Mono<ProductInfo> getProductInfo(String productId) {
        return this.getProductById(productId)
                .map(this.productAdapter::toModel);
    }

    @Override
    public Flux<String> getAllProductIds() {
        return this.getAllProducts()
                .map(ProductDto::id);
    }

    private Flux<ProductDto> getAllProducts() {
        return this.webClient.get()
                .uri("/all")
                .retrieve()
                .bodyToFlux(ProductDto.class)
                .timeout(Duration.ofSeconds(this.timeoutSeconds))
                .retryWhen(this.handleRetry())
                .onErrorMap(Exceptions::isRetryExhausted, this::handleRetriedErrors);
    }

    private Mono<ProductDto> getProductById(String productId) {
        return this.webClient.get()
                .uri("/{id}", productId)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, this::handleClientError)
                .bodyToMono(ProductDto.class)
                .timeout(Duration.ofSeconds(this.timeoutSeconds))
                .retryWhen(this.handleRetry())
                .onErrorMap(Exceptions::isRetryExhausted, this::handleRetriedErrors);
    }

    private Mono<OrderProcessingException> handleClientError(ClientResponse clientResponse) {

        Function<WebClientResponseException, String> buildExplanationMessage = err ->
                "Status: " + err.getStatusCode().value() + "; Body: " + err.getResponseBodyAsString();

        return clientResponse
                .createException()
                .map(err -> new OrderProcessingException
                        (
                                "Invalid Product Request",
                                buildExplanationMessage.apply(err),
                                err
                        )
                );
    }

    private OrderProcessingException handleRetriedErrors(Throwable err) {
        return new OrderProcessingException("Product Service Error", err.getCause().getMessage(), err);
    }

    private RetryBackoffSpec handleRetry() {
        return Retry.fixedDelay(this.retryAttempts, Duration.ofSeconds(this.retryFixedDelaySeconds))
                .filter(this::isRetriable)
                .doAfterRetry(sg -> log.warn("Retrying request to Product service. Attempt {}", sg.totalRetries() + 1));
    }

    private boolean isRetriable(Throwable throwable) {
        return this.exceptionUtil.isServer5xxError()
                .or(this.exceptionUtil.isTimeoutError())
                .or(this.exceptionUtil.isWebRequestException())
                .test(throwable);
    }

}
