package com.rp.webfluxdemo.webtestclient;

import com.rp.webfluxdemo.infrastructure.service.MathService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;

@WebFluxTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class Lec05WebTestClientBindToServerTest {

    // can also be used to get mock data/objects from an API
    private WebTestClient webTestClient;

    @MockBean
    private MathService mathServiceMock;

    @BeforeAll
    public void setClient() {
        this.webTestClient = WebTestClient
                .bindToServer()
                .baseUrl("http://external-api/mocks")
                .build();
    }

    @Test
    public void test() {

    }

}
