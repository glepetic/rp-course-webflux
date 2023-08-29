package com.rp.webfluxdemo.webclient.lecture;

import com.rp.webfluxdemo.infrastructure.dto.response.ResultResponse;
import com.rp.webfluxdemo.webclient.BaseTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class Lec01GetSingleResponseTest extends BaseTest {

    @Autowired
    private WebClient webClient;

    @Test
    public void blockTest() {

        // Assemble
        String squareUri = "/math/square/{input}";
        long input = 10;

        // Act
        ResultResponse result = this.webClient
                .get()
                .uri(squareUri, input)
                .retrieve()
                .bodyToMono(ResultResponse.class)
                .block();

        // Assert
        Assertions.assertNotNull(result);
        Assertions.assertEquals(100L, result.result());

    }

    @Test
    public void stepVerifierTest() {

        // Assemble
        String squareUri = "/math/square/{input}";
        long input = 10;

        // Act
        Mono<ResultResponse> result = this.webClient
                .get()
                .uri(squareUri, input)
                .retrieve()
                .bodyToMono(ResultResponse.class);

        // Assert
        StepVerifier.create(result)
                .assertNext(output -> Assertions.assertEquals(100, output.result()))
                .verifyComplete();

    }

}
