package com.rp.orderservice.infrastructure.client;

import com.rp.orderservice.adapter.TransactionAdapter;
import com.rp.orderservice.domain.model.TransactionOutcome;
import com.rp.orderservice.domain.port.UserTransactionPort;
import com.rp.orderservice.infrastructure.dto.integration.user.TransactionDto;
import com.rp.orderservice.infrastructure.dto.integration.user.TransactionResultDto;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public class UserTransactionClient implements UserTransactionPort {

    private final String transactionsUri;

    private final WebClient webClient;
    private final TransactionAdapter transactionAdapter;

    public UserTransactionClient(String transactionsUri,
                                 WebClient webClient,
                                 TransactionAdapter transactionAdapter) {
        this.transactionsUri = transactionsUri;
        this.webClient = webClient;
        this.transactionAdapter = transactionAdapter;
    }

    @Override
    public Mono<TransactionOutcome> execute(long userId, int amount) {
        return Mono.fromSupplier(() -> this.transactionAdapter.toDto(userId, amount))
                .flatMap(this::callUserService)
                .map(this.transactionAdapter::toModel);
    }

    private Mono<TransactionResultDto> callUserService(TransactionDto transactionDto) {
        return this.webClient.post()
                .uri(this.transactionsUri)
                .bodyValue(transactionDto)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        webClientResponse -> webClientResponse.createException()
                                .map(err -> null))
                .onStatus(HttpStatusCode::isError,
                        webClientResponse -> webClientResponse.createException()
                                .map(err -> null))
                .bodyToMono(TransactionResultDto.class);
    }

}
