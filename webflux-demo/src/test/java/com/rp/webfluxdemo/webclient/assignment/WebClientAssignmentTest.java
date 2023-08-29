package com.rp.webfluxdemo.webclient.assignment;

import com.rp.webfluxdemo.infrastructure.dto.response.AssignmentResponse;
import com.rp.webfluxdemo.webclient.BaseTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class WebClientAssignmentTest extends BaseTest {

    private static final String TEMPLATE = "%d %s %d = %s%n";

    @Autowired
    private WebClient webClient;

    @Test
    public void assignmentTest() {

        // Assemble
        long first = 2L;
        long second = 4L;

        // Act
        Mono<AssignmentResponse> sum = this.send("+", first, second);
        Mono<AssignmentResponse> substraction = this.send("-", first, second);
        Mono<AssignmentResponse> multiplication = this.send("*", first, second);
        Mono<AssignmentResponse> division = this.send("/", first, second);
        Flux<AssignmentResponse> results = Flux.concat(sum, substraction, multiplication, division);

        // Assert
        StepVerifier.create(results)
                .assertNext(response -> Assertions.assertEquals(6, response.result()))
                .assertNext(response -> Assertions.assertEquals(-2, response.result()))
                .assertNext(response -> Assertions.assertEquals(8, response.result()))
                .assertNext(response -> Assertions.assertEquals(0.5, response.result()))
                .verifyComplete();

    }

    private Mono<AssignmentResponse> send(String operation, long first, long second) {
        return this.webClient
                .get()
                .uri("/assignment/calculator/{first}/{second}", first, second)
                .header("OP", operation)
                .retrieve()
                .bodyToMono(AssignmentResponse.class)
                .doOnNext(response -> System.out.printf(TEMPLATE, first, operation, second, response.result()));
    }

}
