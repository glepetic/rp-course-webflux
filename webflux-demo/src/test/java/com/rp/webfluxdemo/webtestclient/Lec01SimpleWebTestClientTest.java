package com.rp.webfluxdemo.webtestclient;

import com.rp.webfluxdemo.infrastructure.dto.response.ResultResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

@SpringBootTest // eagerly configures all beans and spring context
@AutoConfigureWebTestClient
public class Lec01SimpleWebTestClientTest {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    public void simpleTest() {

        // Assemble
        String squareUri = "/math/square/{input}";
        long input = 10;

        // Act
        WebTestClient.ResponseSpec result = this.webTestClient
                .get()
                .uri(squareUri, input)
                .exchange();

        // Assert
        Flux<ResultResponse> resultFlux = result.expectStatus().is2xxSuccessful()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .returnResult(ResultResponse.class)
                .getResponseBody();

        StepVerifier.create(resultFlux)
                .assertNext(output -> Assertions.assertEquals(100, output.result()))
                .verifyComplete();

    }

    @Test
    public void fluentAssertionTest() {

        // Assemble
        String squareUri = "/math/square/{input}";
        long input = 10;

        // Act
        WebTestClient.ResponseSpec result = this.webTestClient
                .get()
                .uri(squareUri, input)
                .exchange();

        // Assert
        result.expectStatus().is2xxSuccessful()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(ResultResponse.class)
                .value(resultResponse -> Assertions.assertEquals(100, resultResponse.result()));

    }

}
