package com.rp.orderservice.infrastructure.client;

import com.rp.orderservice.adapter.TransactionAdapter;
import com.rp.orderservice.domain.exception.OrderProcessingException;
import com.rp.orderservice.domain.model.TransactionOutcome;
import com.rp.orderservice.domain.port.UserPort;
import com.rp.orderservice.infrastructure.dto.integration.user.TransactionDto;
import com.rp.orderservice.infrastructure.dto.integration.user.TransactionResultDto;
import com.rp.orderservice.infrastructure.dto.integration.user.UserDto;
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
public class UserClient implements UserPort {

    private final String transactionsUri;
    private final int timeoutSeconds;
    private final int retryAttempts;
    private final int retryFixedDelaySeconds;

    private final WebClient webClient;
    private final TransactionAdapter transactionAdapter;
    private final ExceptionUtil exceptionUtil;

    public UserClient(String transactionsUri,
                      int timeoutSeconds,
                      int retryAttempts,
                      int retryFixedDelaySeconds,
                      WebClient webClient,
                      TransactionAdapter transactionAdapter,
                      ExceptionUtil exceptionUtil) {
        this.transactionsUri = transactionsUri;
        this.timeoutSeconds = timeoutSeconds;
        this.retryAttempts = retryAttempts;
        this.retryFixedDelaySeconds = retryFixedDelaySeconds;
        this.webClient = webClient;
        this.transactionAdapter = transactionAdapter;
        this.exceptionUtil = exceptionUtil;
    }

    @Override
    public Mono<TransactionOutcome> executeTransaction(long userId, int amount) {
        return Mono.fromSupplier(() -> this.transactionAdapter.toDto(userId, amount))
                .flatMap(this::executeTransaction)
                .map(this.transactionAdapter::toModel);
    }

    @Override
    public Flux<Long> getAllUserIds() {
        return this.getAllUsers()
                .map(UserDto::id);
    }

    private Flux<UserDto> getAllUsers() {
        return this.webClient.get()
                .retrieve()
                .bodyToFlux(UserDto.class)
                .timeout(Duration.ofSeconds(this.timeoutSeconds))
                .retryWhen(this.handleRetry())
                .onErrorMap(Exceptions::isRetryExhausted, this::handleRetriedErrors);
    }

    private Mono<TransactionResultDto> executeTransaction(TransactionDto transactionDto) {
        return this.webClient.post()
                .uri(this.transactionsUri)
                .bodyValue(transactionDto)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, this::handleClientError)
                .bodyToMono(TransactionResultDto.class)
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
                                "Invalid Transaction Request",
                                buildExplanationMessage.apply(err),
                                err
                        )
                );
    }

    private OrderProcessingException handleRetriedErrors(Throwable err) {
        return new OrderProcessingException("User Service Error", err.getCause().getMessage(), err);
    }

    private RetryBackoffSpec handleRetry() {
        return Retry.fixedDelay(this.retryAttempts, Duration.ofSeconds(this.retryFixedDelaySeconds))
                .filter(this::isRetriable)
                .doAfterRetry(sg -> log.warn("Retrying request to User service. Attempt {}", sg.totalRetries() + 1));
    }

    private boolean isRetriable(Throwable throwable) {
        return this.exceptionUtil.isServer5xxError()
                .or(this.exceptionUtil.isTimeoutError())
                .or(this.exceptionUtil.isWebRequestException())
                .test(throwable);
    }

}
