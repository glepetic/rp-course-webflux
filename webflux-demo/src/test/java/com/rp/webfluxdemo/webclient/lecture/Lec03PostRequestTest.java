package com.rp.webfluxdemo.webclient.lecture;

import com.rp.webfluxdemo.infrastructure.dto.request.MultiplicationRequest;
import com.rp.webfluxdemo.infrastructure.dto.response.ResultResponse;
import com.rp.webfluxdemo.infrastructure.dto.response.SimpleIdResponse;
import com.rp.webfluxdemo.webclient.BaseTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class Lec03PostRequestTest extends BaseTest {

    @Autowired
    private WebClient webClient;

    @Test
    public void postTest() {

        // Assemble
        String tableUri = "/math/multiplication";
        Mono<MultiplicationRequest> reqMono = Mono.fromSupplier(() -> new MultiplicationRequest(5, 7));

        // Act
        Mono<SimpleIdResponse> simpleIdResponse = this.webClient
                .post()
                .uri(tableUri)
                .body(reqMono, MultiplicationRequest.class)
                .retrieve()
                .bodyToMono(SimpleIdResponse.class)
                .cache();

        Mono<ResultResponse> result = simpleIdResponse
                .flatMap(simpleResponse -> this.webClient.get()
                        .uri("/math/multiplication/{id}/result", simpleResponse.id())
                        .retrieve()
                        .bodyToMono(ResultResponse.class)
                );

        // Assert
        StepVerifier.create(simpleIdResponse)
                .assertNext(response -> {
                    Assertions.assertNotNull(response.id());
                    Assertions.assertTrue(response.id().matches("\\w{8}-\\w{4}-\\w{4}-\\w{4}-\\w{12}"));
                })
                .verifyComplete();

        StepVerifier.create(result)
                .assertNext(resultResponse -> Assertions.assertEquals(35, resultResponse.result()))
                .verifyComplete();

    }

}
