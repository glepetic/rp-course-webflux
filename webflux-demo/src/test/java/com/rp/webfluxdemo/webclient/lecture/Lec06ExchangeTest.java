package com.rp.webfluxdemo.webclient.lecture;

import com.rp.webfluxdemo.infrastructure.dto.response.ErrorResponse;
import com.rp.webfluxdemo.infrastructure.dto.response.ResultResponse;
import com.rp.webfluxdemo.webclient.BaseTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class Lec06ExchangeTest extends BaseTest {

    @Autowired
    private WebClient webClient;

    @Test
    public void badRequestExchangeTest() {

        // Assemble
        Long input = 5L;
        String expectedMessage = String.format("Valid range is 10 to 20. Input %s is out of range", input);

        // Act
        Mono<Object> response = this.webClient
                .get()
                .uri("/math/square/{number}/manual", input)
                .exchangeToMono(this::exchange)
                .doOnNext(System.out::println);

        // Assert
        StepVerifier.create(response)
                .assertNext(errorResp -> {
                    Assertions.assertEquals(ErrorResponse.class, errorResp.getClass());
                    ErrorResponse resp = (ErrorResponse) errorResp;
                    Assertions.assertEquals(100, resp.code());
                    Assertions.assertEquals(expectedMessage, resp.message());
                })
                .verifyComplete();

    }

    // exchange = retrieve + more info (obtained through ClientResponse object)
    private Mono<Object> exchange(ClientResponse response) {
        Mono<Object> obj = Mono.just(response)
                .filter(resp -> resp.statusCode().is4xxClientError())
                .flatMap(resp -> resp.bodyToMono(ErrorResponse.class));
        return obj.switchIfEmpty(Mono.defer(() -> response.bodyToMono(ResultResponse.class)));
    }

}
