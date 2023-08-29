package com.rp.webfluxdemo.webclient.lecture;

import com.rp.webfluxdemo.infrastructure.dto.request.MultiplicationRequest;
import com.rp.webfluxdemo.infrastructure.dto.response.SimpleIdResponse;
import com.rp.webfluxdemo.webclient.BaseTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class Lec08AttributesTest extends BaseTest {

    @Autowired
    private WebClient webClientWithDynamicAuth;

    @Test
    public void basicAuthHeaderTest() {

        // Assemble
        String tableUri = "/math/multiplication";
        Mono<MultiplicationRequest> reqMono = Mono.fromSupplier(() -> new MultiplicationRequest(5, 7));
        String authorizationHeaderName = "Authorization";

        // Act
        Mono<ResponseEntity<SimpleIdResponse>> basicResponse = this.webClientWithDynamicAuth
                .post()
                .uri(tableUri)
                .attribute("auth", "basic")
                .body(reqMono, MultiplicationRequest.class)
                .retrieve()
                .toEntity(SimpleIdResponse.class);

        // Assert
        StepVerifier.create(basicResponse)
                .assertNext(resp -> {
                    Assertions.assertTrue(resp.getHeaders().containsKey(authorizationHeaderName));
                    String auth = resp.getHeaders().getFirst(authorizationHeaderName);
                    Assertions.assertNotNull(auth);
                    Assertions.assertTrue(auth.contains("Basic"));
                })
                .verifyComplete();

    }

    @Test
    public void oauthHeaderTest() {

        // Assemble
        String tableUri = "/math/multiplication";
        Mono<MultiplicationRequest> reqMono = Mono.fromSupplier(() -> new MultiplicationRequest(5, 7));
        String authorizationHeaderName = "Authorization";
        String expectedTokenPattern = "\\w{8}-\\w{4}-\\w{4}-\\w{4}-\\w{12}";

        // Act
        Mono<ResponseEntity<SimpleIdResponse>> basicResponse = this.webClientWithDynamicAuth
                .post()
                .uri(tableUri)
                .attribute("auth", "oauth")
                .body(reqMono, MultiplicationRequest.class)
                .retrieve()
                .toEntity(SimpleIdResponse.class);

        // Assert
        StepVerifier.create(basicResponse)
                .assertNext(resp -> {
                    HttpHeaders headers = resp.getHeaders();
                    Assertions.assertTrue(headers.containsKey(authorizationHeaderName));
                    String auth = headers.getFirst(authorizationHeaderName);
                    Assertions.assertNotNull(auth);
                    Assertions.assertTrue(auth.contains("Bearer"));
                    String token = auth.replace("Bearer", "").trim();
                    Assertions.assertTrue(token.matches(expectedTokenPattern));
                })
                .verifyComplete();

    }

    @Test
    public void unmatchedHeaderTest() {

        // Assemble
        String tableUri = "/math/multiplication";
        Mono<MultiplicationRequest> reqMono = Mono.fromSupplier(() -> new MultiplicationRequest(5, 7));
        String authorizationHeaderName = "Authorization";

        // Act
        Mono<ResponseEntity<SimpleIdResponse>> basicResponse = this.webClientWithDynamicAuth
                .post()
                .uri(tableUri)
                .attribute("auth", "unmatched")
                .body(reqMono, MultiplicationRequest.class)
                .retrieve()
                .toEntity(SimpleIdResponse.class);

        // Assert
        StepVerifier.create(basicResponse)
                .assertNext(resp -> Assertions.assertFalse(resp.getHeaders().containsKey(authorizationHeaderName)))
                .verifyComplete();

    }

}
