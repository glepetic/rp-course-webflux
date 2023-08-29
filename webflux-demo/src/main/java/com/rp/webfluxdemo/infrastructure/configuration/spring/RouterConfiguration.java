package com.rp.webfluxdemo.infrastructure.configuration.spring;

import com.rp.webfluxdemo.domain.exception.InvalidRangeException;
import com.rp.webfluxdemo.infrastructure.dto.response.ErrorResponse;
import com.rp.webfluxdemo.infrastructure.router.MathRequestHandler;
import com.rp.webfluxdemo.infrastructure.service.MathService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.*;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Supplier;

@Configuration
public class RouterConfiguration {

    @Bean // can have more than one RouterFunction Bean if needed
    public RouterFunction<ServerResponse> highLevelRouter(MathRequestHandler mathRequestHandler) {
        return RouterFunctions.route()
                .path("router", builder -> builder
                        .path("high-level-example", this::lowLevelRouter)
                        .path("request-predicate", this::requestPredicateRouter)
                        .path("math", () -> this.mathResponseRouter(mathRequestHandler)))
                .build();
    }

    private RouterFunction<ServerResponse> lowLevelRouter() {
        return RouterFunctions.route()
                .GET("health", req -> ServerResponse.ok().bodyValue(Map.of("status", "UP")))
                .build();
    }

    private RouterFunction<ServerResponse> requestPredicateRouter() {
        return RouterFunctions.route()
                .GET("{id}",
                        RequestPredicates.path( "/{myId:[\\w]+}"),
                        this::handleValidId)
                .GET("{id}", this::handleInvalidId)
                .build();
    }

    private Mono<ServerResponse> handleValidId(ServerRequest request) {
        Supplier<Map> bodySupplier = () -> Map.of(
                "id", request.pathVariable("id"),
                "valid", true
        );
        return ServerResponse.ok().bodyValue(bodySupplier.get());
    }

    private Mono<ServerResponse> handleInvalidId(ServerRequest request) {
        Supplier<Map> bodySupplier = () -> Map.of(
                "id", request.pathVariable("id"),
                "valid", false
        );
        return ServerResponse.badRequest().bodyValue(bodySupplier.get());
    }

    public RouterFunction<ServerResponse> mathResponseRouter(MathRequestHandler mathRequestHandler) {
        return RouterFunctions.route()
                .GET("square/{number}", mathRequestHandler::squareHandler)
                .GET("table/{number}", mathRequestHandler::tableJsonHandler)
                .GET("table/{number}/stream", mathRequestHandler::tableStreamHandler)
                .POST("multiplication", mathRequestHandler::multiplicationHandler)
                .GET("square/{number}/limited", mathRequestHandler::squareLimitedHandler)
                .onError(InvalidRangeException.class, this.invalidRangeHandler())
                .onError(Throwable.class, this.genericExceptionHandler())
                .build();
    }

    private BiFunction<InvalidRangeException, ServerRequest, Mono<ServerResponse>> invalidRangeHandler() {
        return (err, req) -> ServerResponse.badRequest().bodyValue(new ErrorResponse(100, err.getMessage()));
    }

    private BiFunction<Throwable, ServerRequest, Mono<ServerResponse>> genericExceptionHandler() {
        return (err, req) -> ServerResponse.badRequest().bodyValue(new ErrorResponse(-1, err.getMessage()));
    }

    @Bean
    public MathRequestHandler mathRequestHandler(MathService mathService) {
        return new MathRequestHandler(mathService);
    }

}
