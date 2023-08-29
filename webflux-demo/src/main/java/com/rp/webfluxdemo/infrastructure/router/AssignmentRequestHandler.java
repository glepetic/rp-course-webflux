package com.rp.webfluxdemo.infrastructure.router;

import com.rp.webfluxdemo.infrastructure.dto.response.AssignmentResponse;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.function.BinaryOperator;
import java.util.function.Supplier;

public class AssignmentRequestHandler {

    // No calls to services to simplify assignment

    public Mono<ServerResponse> sumHandler(ServerRequest serverRequest) {
        return this.operationHandler(serverRequest, Double::sum);
    }

    public Mono<ServerResponse> substractionHandler(ServerRequest serverRequest) {
        return this.operationHandler(serverRequest, (first, second) -> first - second);
    }

    public Mono<ServerResponse> multiplicationHandler(ServerRequest serverRequest) {
        return this.operationHandler(serverRequest, (first, second) -> first * second);
    }

    public Mono<ServerResponse> divisionHandler(ServerRequest serverRequest) {
        return this.operationHandler(serverRequest, (first, second) -> first / second);
    }

    private Mono<ServerResponse> operationHandler(ServerRequest serverRequest, BinaryOperator<Double> func) {
        Supplier<Double> firstSupplier = () -> Double.parseDouble(serverRequest.pathVariable("first"));
        Supplier<Double> secondSupplier = () -> Double.parseDouble(serverRequest.pathVariable("second"));
        return Mono.zip(Mono.fromSupplier(firstSupplier), Mono.fromSupplier(secondSupplier))
                .map(pair -> func.apply(pair.getT1(), pair.getT2()))
                .map(AssignmentResponse::new)
                .flatMap(response -> ServerResponse.ok().bodyValue(response));
    }

}
