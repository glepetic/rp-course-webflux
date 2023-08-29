package com.rp.webfluxdemo.infrastructure.router;


import com.rp.webfluxdemo.domain.exception.InvalidRangeException;
import com.rp.webfluxdemo.infrastructure.dto.request.MultiplicationRequest;
import com.rp.webfluxdemo.infrastructure.dto.response.ResultResponse;
import com.rp.webfluxdemo.infrastructure.service.MathService;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;
import java.util.function.Supplier;

public class MathRequestHandler {

    private final MathService mathService;

    public MathRequestHandler(MathService mathService) {
        this.mathService = mathService;
    }

    public Mono<ServerResponse> squareHandler(ServerRequest serverRequest) {
        return Mono.just(serverRequest.pathVariable("number"))
                .map(Long::parseLong)
                .flatMap(this.mathService::getSquare)
                .flatMap(response -> ServerResponse.ok().bodyValue(response));
    }

    public Mono<ServerResponse> tableJsonHandler(ServerRequest serverRequest) {
        return this.tableHandler(serverRequest, MediaType.APPLICATION_JSON);
    }

    public Mono<ServerResponse> tableStreamHandler(ServerRequest serverRequest) {
        return this.tableHandler(serverRequest, MediaType.TEXT_EVENT_STREAM);
    }

    private Mono<ServerResponse> tableHandler(ServerRequest serverRequest, MediaType mediaType) {

        Optional<Integer> from = serverRequest.queryParam("from")
                .map(Integer::parseInt);
        Optional<Integer> to = serverRequest.queryParam("to")
                .map(Integer::parseInt);
        Integer fromValue = from.orElse(1);
        Integer toValue = to.orElseGet(() -> fromValue + 9);

        Flux<ResultResponse> tableFlux = Mono.just(serverRequest.pathVariable("number"))
                .map(Long::parseLong)
                .flatMapMany(number -> this.mathService.getMultiplicationTable(number, fromValue, toValue));

        return ServerResponse.ok()
                .contentType(mediaType)
                .body(tableFlux, ResultResponse.class);
    }

    public Mono<ServerResponse> multiplicationHandler(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(MultiplicationRequest.class)
                .flatMap(this.mathService::createMultiplication)
                .flatMap(response -> ServerResponse.ok().bodyValue(response));
    }

    public Mono<ServerResponse> squareLimitedHandler(ServerRequest serverRequest) {
        Supplier<Long> numberSupplier = () -> Long.parseLong(serverRequest.pathVariable("number"));

        return Mono.fromSupplier(numberSupplier)
                .filter(number -> number >= 10 && number <= 20)
                .flatMap(this.mathService::getSquare)
                .flatMap(response -> ServerResponse.ok().bodyValue(response))
                .switchIfEmpty(Mono.error(() -> new InvalidRangeException(numberSupplier.get())));
    }


}
