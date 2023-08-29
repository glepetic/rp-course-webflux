package com.rp.webfluxdemo.webclient.lecture;

import com.rp.webfluxdemo.infrastructure.dto.response.ResultResponse;
import com.rp.webfluxdemo.webclient.BaseTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.net.URI;
import java.util.Map;

public class Lec07QueryParamsTest extends BaseTest {

    @Autowired
    private WebClient webClient;

    @Test
    @DisplayName("Query Params with query string and variables")
    public void queryParamsOption1Test() {

        // Assemble
        Long input = 2L;
        Integer from = 4;
        Integer to = 5;
        String queryString = "http://localhost:8080/math/multiplication/table/{input}/stream?from={from}&to={to}";
        URI uri = UriComponentsBuilder.fromUriString(queryString)
                .build(input, from, to);

        // Act
        Flux<ResultResponse> response = this.webClient.get()
                .uri(uri)
                .retrieve()
                .bodyToFlux(ResultResponse.class);

        // Assert
        StepVerifier.create(response)
                .assertNext(item -> Assertions.assertEquals(8, item.result()))
                .assertNext(item -> Assertions.assertEquals(10, item.result()))
                .verifyComplete();

    }

    @Test
    @DisplayName("Query params with query and build with variables")
    public void queryParamsOption2Test() {

        // Assemble
        Long input = 5L;
        Integer from = 1;
        Integer to = 4;

        // Act
        Flux<ResultResponse> response = this.webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/math/multiplication/table/{input}/stream")
                        .query("from={from}&to={to}")
                        .build(input, from, to))
                .retrieve()
                .bodyToFlux(ResultResponse.class);

        // Assert
        StepVerifier.create(response)
                .assertNext(item -> Assertions.assertEquals(5, item.result()))
                .assertNext(item -> Assertions.assertEquals(10, item.result()))
                .assertNext(item -> Assertions.assertEquals(15, item.result()))
                .assertNext(item -> Assertions.assertEquals(20, item.result()))
                .verifyComplete();

    }

    @Test
    @DisplayName("Query params with query and build with variables inside map")
    public void queryParamsOption3Test() {

        // Assemble
        long input = 10L;
        int from = 20;
        int to = 25;
        Map<String, String> vars = Map.of(
                "input", Long.toString(input),
                "from", Integer.toString(from),
                "to", Integer.toString(to)
        );

        // Act
        Flux<ResultResponse> response = this.webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/math/multiplication/table/{input}/stream")
                        .query("from={from}&to={to}")
                        .build(vars))
                .retrieve()
                .bodyToFlux(ResultResponse.class);

        // Assert
        StepVerifier.create(response)
                .assertNext(item -> Assertions.assertEquals(200, item.result()))
                .assertNext(item -> Assertions.assertEquals(210, item.result()))
                .assertNext(item -> Assertions.assertEquals(220, item.result()))
                .assertNext(item -> Assertions.assertEquals(230, item.result()))
                .assertNext(item -> Assertions.assertEquals(240, item.result()))
                .assertNext(item -> Assertions.assertEquals(250, item.result()))
                .verifyComplete();

    }

}
