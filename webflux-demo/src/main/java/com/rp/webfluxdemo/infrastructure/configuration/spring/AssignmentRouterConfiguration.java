package com.rp.webfluxdemo.infrastructure.configuration.spring;

import com.rp.webfluxdemo.infrastructure.dto.response.ErrorResponse;
import com.rp.webfluxdemo.infrastructure.router.AssignmentRequestHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.*;

import java.util.Optional;

@Configuration
public class AssignmentRouterConfiguration {

    @Bean
    public RouterFunction<ServerResponse> assignmentRouter(AssignmentRequestHandler requestHandler) {

        return RouterFunctions.route()
                .path("/assignment/calculator", () -> this.calculatorRouter(requestHandler))
                .build();

    }

    private RouterFunction<ServerResponse> calculatorRouter(AssignmentRequestHandler requestHandler) {
        return RouterFunctions.route()
                .GET("/{first}/{second}", this::operationSumPredicate, requestHandler::sumHandler)
                .GET("/{first}/{second}", this::operationSubstractionPredicate, requestHandler::substractionHandler)
                .GET("/{first}/{second}", this::operationMultiplicationPredicate, requestHandler::multiplicationHandler)
                .GET("/{first}/{second}", this::operationDivisionPredicate, requestHandler::divisionHandler)
                .GET("/{first}/{second}",
                        RequestPredicates.all(),
                        req -> ServerResponse.badRequest().bodyValue(new ErrorResponse(300, "OP header not found")))
                .build();
    }

    private Boolean operationSumPredicate(ServerRequest request) {
        return this.operationPredicate(request, "+");
    }

    private Boolean operationSubstractionPredicate(ServerRequest request) {
        return this.operationPredicate(request, "-");
    }

    private Boolean operationMultiplicationPredicate(ServerRequest request) {
        return this.operationPredicate(request, "*");
    }

    private Boolean operationDivisionPredicate(ServerRequest request) {
        return this.operationPredicate(request, "/");
    }

    private Boolean operationPredicate(ServerRequest request, String operation) {
        return Optional.ofNullable(request.headers().firstHeader("OP"))
                .filter(value -> value.equals(operation))
                .isPresent();
    }

    @Bean
    public AssignmentRequestHandler requestHandler() {
        return new AssignmentRequestHandler();
    }

}
