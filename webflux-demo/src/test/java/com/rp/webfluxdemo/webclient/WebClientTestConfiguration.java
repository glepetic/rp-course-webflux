package com.rp.webfluxdemo.webclient;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;

@Configuration
public class WebClientTestConfiguration {

    @Value("${math.api.host}")
    private String mathApiHost;

    @Bean
    public WebClient webClient() {
        return WebClient
                .builder()
                .baseUrl(this.mathApiHost)
                .build();
    }

    @Bean
    public WebClient webClientWithBasicAuth() {
        return this.webClient()
                .mutate()
                .defaultHeaders(headers -> headers.setBasicAuth("myUser", "myPass"))
                .build();
    }

    @Bean
    public WebClient webClientWithJwtAuth() {
        return this.webClient()
                .mutate()
                .filter(this::sessionUUID)
                .build();
    }

    @Bean
    public WebClient webClientWithDynamicAuth() {
        return this.webClient()
                .mutate()
                .filter(this::dynamicSessionAuth)
                .build();
    }

    // authentication and token generation set apart from service or business logic (done in configuration)
    private Mono<ClientResponse> sessionUUID(ClientRequest request, ExchangeFunction ex) {
        String myRandomlyGeneratedJwt = UUID.randomUUID().toString();
        ClientRequest modifiedRequest = ClientRequest.from(request)
                .headers(headers -> headers.setBearerAuth(myRandomlyGeneratedJwt))
                .build();
        return ex.exchange(modifiedRequest);
    }

    private Mono<ClientResponse> dynamicSessionAuth(ClientRequest request, ExchangeFunction ex) {

        Optional<String> authAttribute = request.attribute("auth")
                .map(Object::toString);

        Supplier<Optional<ClientRequest>> basicAuthHandler = () -> authAttribute
                .filter(attribute -> attribute.equalsIgnoreCase("basic"))
                .map(attribute -> this.withBasicAuth(request));

        Supplier<Optional<ClientRequest>> oauthHandler = () -> authAttribute
                .filter(attribute -> attribute.equalsIgnoreCase("oauth"))
                .map(attribute -> this.withOAuth(request));

        ClientRequest modifiedRequest = basicAuthHandler.get()
                .or(oauthHandler)
                .orElse(request);

        return ex.exchange(modifiedRequest);
    }

    private ClientRequest withBasicAuth(ClientRequest request) {
        return ClientRequest.from(request)
                .headers(headers -> headers.setBasicAuth("myUser", "myPassword"))
                .build();
    }

    private ClientRequest withOAuth(ClientRequest request) {
        String myRandomlyGeneratedJwt = UUID.randomUUID().toString();
        return ClientRequest.from(request)
                .headers(headers -> headers.setBearerAuth(myRandomlyGeneratedJwt))
                .build();
    }

}
