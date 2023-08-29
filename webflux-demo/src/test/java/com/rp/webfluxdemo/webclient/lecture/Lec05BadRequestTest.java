package com.rp.webfluxdemo.webclient.lecture;

import com.rp.webfluxdemo.infrastructure.dto.response.ResultResponse;
import com.rp.webfluxdemo.webclient.BaseTest;
import com.rp.webfluxdemo.webclient.testUtil.ExternalApiException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class Lec05BadRequestTest extends BaseTest {

    @Autowired
    private WebClient webClient;

    @Test
    public void badRequestTest() {

        Long input = 5L;

        // Act
        Mono<ResultResponse> response = this.webClient
                .get()
                .uri("/math/square/{number}/manual", input)
                .retrieve()
                .bodyToMono(ResultResponse.class);

        // Assemble
        StepVerifier.create(response)
                .expectError(WebClientResponseException.BadRequest.class)
                .verify();

    }

    @Test
    public void handleBadRequestTest() {

        // Assemble
        Long input = 5L;

        // Act
        Mono<ResultResponse> response = this.webClient
                .get()
                .uri("/math/square/{number}/manual", input)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, clientResponse -> clientResponse
                        .createException()
                        .map(err -> new ExternalApiException(err.getResponseBodyAsString()))
                )
                .bodyToMono(ResultResponse.class);

        // Assert
        StepVerifier.create(response)
                .expectError(ExternalApiException.class)
                .verify();

    }

}
