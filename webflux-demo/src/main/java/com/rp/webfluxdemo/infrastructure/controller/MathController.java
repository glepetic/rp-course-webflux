package com.rp.webfluxdemo.infrastructure.controller;

import com.rp.webfluxdemo.domain.exception.InvalidRangeException;
import com.rp.webfluxdemo.infrastructure.dto.request.MultiplicationRequest;
import com.rp.webfluxdemo.infrastructure.dto.response.ResultResponse;
import com.rp.webfluxdemo.infrastructure.dto.response.SimpleIdResponse;
import com.rp.webfluxdemo.infrastructure.service.MathService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/math")
public class MathController {

    private final MathService mathService;

    @Autowired
    public MathController(MathService mathService) {
        this.mathService = mathService;
    }

    @GetMapping("/square/{number}")
    public Mono<ResultResponse> getSquare(@PathVariable Long number) {
        return this.mathService.getSquare(number);
    }

    @GetMapping("/square/{number}/manual")
    public Mono<ResultResponse> getSquareWithLimitedRange(@PathVariable Long number) {
        return Mono.just(number)
                .handle((input, sink) -> {
                    if (input < 10 || input > 20) sink.error(new InvalidRangeException(input));
                    else sink.next(input);
                })
                .cast(Long.class)
                .flatMap(this.mathService::getSquare);
    }

    @GetMapping("/multiplication/table/{number}")
    public Flux<ResultResponse> getMultiplicationTable(@PathVariable Long number,
                                                       @RequestParam(required = false) Optional<Integer> from,
                                                       @RequestParam(required = false) Optional<Integer> to) {
        // accumulates/collects items into a List (Mono<List>) and then converts them to json to push them all downstream
        return this.multiplicationTable(number, from, to);
    }

    @GetMapping(value = "/multiplication/table/{number}/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ResultResponse> getMultiplicationTableStream(@PathVariable Long number,
                                                             @RequestParam(required = false) Optional<Integer> from,
                                                             @RequestParam(required = false) Optional<Integer> to) {
        // pushes item downstream as they are processed
        return this.multiplicationTable(number, from, to);
    }

    private Flux<ResultResponse> multiplicationTable(Long number,
                                                     Optional<Integer> from,
                                                     Optional<Integer> to) {
        Integer fromValue = from.orElse(1);
        Integer toValue = to.orElseGet(() -> fromValue + 9);
        return this.mathService.getMultiplicationTable(number, fromValue, toValue);
    }

    @PostMapping("/multiplication") // use Mono for requestBody to read it in non blocking way
    public Mono<ResponseEntity<SimpleIdResponse>> createMultiplication(@RequestBody Mono<MultiplicationRequest> multiplicationRequest,
                                                                       @RequestHeader Map<String, String> headers) {
        HttpHeaders responseHeaders = new HttpHeaders();
        Map<String, List<String>> exampleHeaders = headers
                .entrySet()
                .stream()
                .filter(header -> header.getKey().startsWith("example-") || header.getKey().equalsIgnoreCase("authorization"))
                .collect(Collectors.toMap(Map.Entry::getKey, e -> Collections.singletonList(e.getValue())));
        responseHeaders.putAll(exampleHeaders);
        return multiplicationRequest
                .flatMap(this.mathService::createMultiplication)
                .map(simpleIdResponse -> ResponseEntity
                        .status(HttpStatus.CREATED)
                        .headers(responseHeaders)
                        .body(simpleIdResponse));
    }

    @GetMapping("/multiplication/{id}/result")
    public Mono<ResultResponse> getMultiplicationResult(@PathVariable String id) {
        return this.mathService.getMultiplicationResult(id);
    }

}
