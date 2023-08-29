package com.rp.webfluxdemo.infrastructure.controller;

import com.rp.webfluxdemo.infrastructure.dto.response.ResultResponse;
import com.rp.webfluxdemo.infrastructure.service.MathService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/assignment/math")
public class AssignmentController {

    private final MathService mathService;

    @Autowired
    public AssignmentController(MathService mathService) {
        this.mathService = mathService;
    }

    @GetMapping("/square/{number}")
    public Mono<ResponseEntity<ResultResponse>> getSquare(@PathVariable Long number) {
        return Mono.just(number)
                .filter(input -> input >= 10 && input <= 20)
                .flatMap(this.mathService::getSquare)
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.fromSupplier(() -> ResponseEntity.badRequest().build()));
    }

}
