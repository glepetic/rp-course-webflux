package com.rp.webfluxdemo.webtestclient;

import com.rp.webfluxdemo.infrastructure.controller.MathController;
import com.rp.webfluxdemo.infrastructure.dto.request.MultiplicationRequest;
import com.rp.webfluxdemo.infrastructure.dto.response.SimpleIdResponse;
import com.rp.webfluxdemo.infrastructure.service.MathService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.UUID;

@WebFluxTest(MathController.class)
public class Lec03ControllerPostTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private MathService mathServiceMock;

    @Test
    public void postTest() {

        // Assemble
        String uuid = UUID.randomUUID().toString();
        Mono<SimpleIdResponse> mockedIdResponse = Mono.just(new SimpleIdResponse(uuid));
        MultiplicationRequest request = new MultiplicationRequest(5,6);
        Mono<MultiplicationRequest> requestBody = Mono.just(request);
        Mockito.when(this.mathServiceMock.createMultiplication(request))
                .thenReturn(mockedIdResponse);

        // Act
        WebTestClient.ResponseSpec result = this.webTestClient.post()
                .uri("/math/multiplication")
                .accept(MediaType.APPLICATION_JSON)
                .headers(headers -> headers.setBasicAuth("myUser", "myPassword"))
                .body(requestBody, MultiplicationRequest.class)
                .exchange();

        // Assert
        result.expectStatus().is2xxSuccessful()
                .expectHeader().exists("Authorization")
                .expectBody(SimpleIdResponse.class)
                .value(simpleIdResponse -> Assertions.assertEquals(uuid, simpleIdResponse.id()));

    }


}
