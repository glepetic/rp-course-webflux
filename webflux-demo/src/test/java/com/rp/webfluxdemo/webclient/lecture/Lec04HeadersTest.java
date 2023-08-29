package com.rp.webfluxdemo.webclient.lecture;

import com.rp.webfluxdemo.infrastructure.dto.request.MultiplicationRequest;
import com.rp.webfluxdemo.infrastructure.dto.response.SimpleIdResponse;
import com.rp.webfluxdemo.webclient.BaseTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class Lec04HeadersTest extends BaseTest {

    @Autowired
    private WebClient webClient;

    @Test
    public void headersTest() {

        // Assemble
        String tableUri = "/math/multiplication";
        Mono<MultiplicationRequest> reqMono = Mono.fromSupplier(() -> new MultiplicationRequest(5, 7));
        String header = "example-header";
        String headerValue = "example-value";

        // Act
        Mono<ResponseEntity<SimpleIdResponse>> response = this.webClient
                .post()
                .uri(tableUri)
                .header(header, headerValue)
                .body(reqMono, MultiplicationRequest.class)
                .retrieve()
                .toEntity(SimpleIdResponse.class);

        // Assert
        StepVerifier.create(response)
                .assertNext(resp -> {
                    Assertions.assertTrue(resp.getHeaders().containsKey(header));
                    Assertions.assertEquals(headerValue, resp.getHeaders().getFirst(header));
                })
                .verifyComplete();

    }

}
