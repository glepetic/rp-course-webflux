package com.rp.webfluxdemo.webtestclient;

import com.rp.webfluxdemo.infrastructure.configuration.spring.RouterConfiguration;
import com.rp.webfluxdemo.infrastructure.dto.response.ResultResponse;
import com.rp.webfluxdemo.infrastructure.router.MathRequestHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@WebFluxTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ContextConfiguration(classes = {RouterConfiguration.class})
public class Lec05RouterFunctionBindContextTest {

    private WebTestClient webTestClient;

    @Autowired
    private ApplicationContext applicationContext;

    @MockBean
    private MathRequestHandler mathRequestHandlerMock;

    @BeforeAll
    public void setClient() {
        this.webTestClient = WebTestClient
                .bindToApplicationContext(this.applicationContext)
                .build();
    }

    @Test
    public void test() {

        // Assemble
        long input = 6;
        long expected = 36;
        Mockito.when(this.mathRequestHandlerMock.squareHandler(Mockito.any()))
                .thenReturn(ServerResponse.ok().bodyValue(new ResultResponse(LocalDateTime.now(), expected)));

        // Act
        WebTestClient.ResponseSpec result = this.webTestClient.get()
                .uri("/router/math/square/{input}", input)
                .exchange();

        // Assert
        result
                .expectStatus().is2xxSuccessful()
                .expectBody(ResultResponse.class)
                .value(resultResponse -> Assertions.assertEquals(expected, resultResponse.result()));

    }

}


