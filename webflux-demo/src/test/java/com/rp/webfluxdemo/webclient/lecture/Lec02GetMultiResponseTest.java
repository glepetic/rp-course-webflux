package com.rp.webfluxdemo.webclient.lecture;

import com.rp.webfluxdemo.infrastructure.dto.response.ResultResponse;
import com.rp.webfluxdemo.webclient.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

public class Lec02GetMultiResponseTest extends BaseTest {

    @Autowired
    private WebClient webClient;

    @Test
    public void fluxTest() {

        // Assemble
        String tableUri = "/math/multiplication/table/{input}";
        long input = 10;

        // Act
        Flux<ResultResponse> result = this.webClient
                .get()
                .uri(tableUri, input)
                .retrieve()
                .bodyToFlux(ResultResponse.class)
                .doOnNext(System.out::println);

        // Assert
        StepVerifier.create(result)
                .expectNextCount(10)
                .verifyComplete();

    }

    @Test
    public void fluxStreamTest() {

        // Assemble
        String tableUri = "/math/multiplication/table/{input}/stream";
        long input = 10;

        // Act
        Flux<ResultResponse> result = this.webClient
                .get()
                .uri(tableUri, input)
                .retrieve()
                .bodyToFlux(ResultResponse.class)
                .doOnNext(System.out::println);

        // Assert
        StepVerifier.create(result.log())
                .expectNextCount(10)
                .verifyComplete();

    }

}
