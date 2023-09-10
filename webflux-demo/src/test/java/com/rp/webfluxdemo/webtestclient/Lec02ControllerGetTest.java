package com.rp.webfluxdemo.webtestclient;

import com.rp.webfluxdemo.infrastructure.controller.MathController;
import com.rp.webfluxdemo.infrastructure.dto.response.ErrorResponse;
import com.rp.webfluxdemo.infrastructure.dto.response.ResultResponse;
import com.rp.webfluxdemo.infrastructure.service.MathService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;

@WebFluxTest(MathController.class)
public class Lec02ControllerGetTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private MathService mathServiceMock;

    @Test
    public void mockingTest() {

        // Assemble
        String squareUri = "/math/square/{input}";
        long input = 10;
        Mockito.when(this.mathServiceMock.getSquare(input))
                .thenReturn(Mono.just(new ResultResponse(LocalDateTime.now(), 100L)));

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

    @Test
    public void mockingEmptyTest() {

        // Assemble
        String squareUri = "/math/square/{input}";
        long input = 10;
        Mockito.when(this.mathServiceMock.getSquare(input))
                .thenReturn(Mono.empty());

        // Act
        WebTestClient.ResponseSpec result = this.webTestClient
                .get()
                .uri(squareUri, input)
                .exchange();

        // Assert
        result.expectStatus().is2xxSuccessful()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(ResultResponse.class)
                .value(resultResponse -> Assertions.assertEquals(-1, resultResponse.result()));

    }

    @Test
    public void listResponseTest() {

        // Assemble
        String tableUri = "/math/multiplication/table/{input}";
        long input = 10;
        Flux<ResultResponse> flux = Flux.range(1, 3)
                .map(i -> new ResultResponse(LocalDateTime.now(), i.longValue()));
        Mockito.when(this.mathServiceMock.getMultiplicationTable(input, 1, 10))
                .thenReturn(flux);

        // Act
        WebTestClient.ResponseSpec result = this.webTestClient
                .get()
                .uri(tableUri, input)
                .exchange();

        // Assert
        result.expectStatus().is2xxSuccessful()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(ResultResponse.class)
                .hasSize(3);

    }

    @Test
    public void listErrorResponseTest() {

        // Assemble
        String tableUri = "/math/multiplication/table/{input}";
        long input = 10;
        String errorMessage = "oops";
        Mockito.when(this.mathServiceMock.getMultiplicationTable(input, 1, 10))
                .thenReturn(Flux.error(() -> new RuntimeException(errorMessage)));

        // Act
        WebTestClient.ResponseSpec result = this.webTestClient
                .get()
                .uri(tableUri, input)
                .exchange();

        // Assert
        result.expectStatus().is5xxServerError()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(ErrorResponse.class)
                .value(errorResponse -> {
                    Assertions.assertEquals(-1, errorResponse.code());
                    Assertions.assertEquals(errorMessage, errorResponse.message());
                });

    }

    @Test
    public void streamingResponseTest() {

        // Assemble
        String tableUri = "/math/multiplication/table/{input}/stream";
        long input = 10;
        Flux<ResultResponse> flux = Flux.range(1, 3)
                .map(i -> new ResultResponse(LocalDateTime.now(), i.longValue()))
                .delayElements(Duration.ofMillis(500));
        Mockito.when(this.mathServiceMock.getMultiplicationTable(input, 1, 10))
                .thenReturn(flux);

        // Act
        WebTestClient.ResponseSpec result = this.webTestClient
                .get()
                .uri(tableUri, input)
                .exchange();

        // Assert
        result.expectStatus().is2xxSuccessful()
                .expectHeader().contentTypeCompatibleWith(MediaType.TEXT_EVENT_STREAM)
                .expectBodyList(ResultResponse.class)
                .hasSize(3);

    }

    @Test
    public void paramsTest() {

        // Assemble
        long input = 10L;
        int from = 20;
        int to = 25;
        Map<String, String> vars = Map.of(
                "input", Long.toString(input),
                "from", Integer.toString(from),
                "to", Integer.toString(to)
        );
        Flux<ResultResponse> flux = Flux.range(1, 5)
                .map(i -> new ResultResponse(LocalDateTime.now(), i.longValue()))
                .delayElements(Duration.ofMillis(500));
        Mockito.when(this.mathServiceMock.getMultiplicationTable(input, from, to)).thenReturn(flux);

        // Act
        WebTestClient.ResponseSpec result = this.webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path("/math/multiplication/table/{input}/stream")
                        .query("from={from}&to={to}")
                        .build(vars))
                .exchange();

        // Assert
        result
                .expectStatus().is2xxSuccessful()
                .expectHeader().contentTypeCompatibleWith(MediaType.TEXT_EVENT_STREAM)
                .expectBodyList(ResultResponse.class)
                .hasSize(5);

    }

}
