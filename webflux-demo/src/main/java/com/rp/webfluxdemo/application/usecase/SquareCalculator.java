package com.rp.webfluxdemo.application.usecase;

import com.rp.webfluxdemo.domain.service.CalculatorService;
import reactor.core.publisher.Mono;

public class SquareCalculator {

    private final CalculatorService calculatorService;

    public SquareCalculator(CalculatorService calculatorService) {
        this.calculatorService = calculatorService;
    }

    public Mono<Long> calculateSquare(Long number) {
        return this.calculatorService.calculatePower(number, 2);
    }

}
