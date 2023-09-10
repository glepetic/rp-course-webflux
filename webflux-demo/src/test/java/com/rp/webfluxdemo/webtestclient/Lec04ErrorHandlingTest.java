package com.rp.webfluxdemo.webtestclient;

import com.rp.webfluxdemo.infrastructure.service.MathService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.Map;

@WebFluxTest
public class Lec04ErrorHandlingTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private MathService mathServiceMock;

    @Test
    public void errorHandlingTest() {

        // Assemble
        long input = 10L;
        String from = "NaN";
        int to = 25;
        Map<String, String> vars = Map.of(
                "input", Long.toString(input),
                "from", from,
                "to", Integer.toString(to)
        );

        // Act
        WebTestClient.ResponseSpec result = this.webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path("/math/multiplication/table/{input}/stream")
                        .query("from={from}&to={to}")
                        .build(vars))
                .exchange();

        // Assert
        result
                .expectStatus().is5xxServerError()
                .expectBody()
                .jsonPath("$.message").isNotEmpty()
                .jsonPath("$.code").isEqualTo(-1);


    }

}
