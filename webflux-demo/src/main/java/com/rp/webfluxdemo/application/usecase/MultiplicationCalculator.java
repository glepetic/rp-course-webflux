package com.rp.webfluxdemo.application.usecase;

import com.rp.webfluxdemo.application.service.ValidationService;
import com.rp.webfluxdemo.domain.model.Multiplication;
import com.rp.webfluxdemo.domain.model.Range;
import com.rp.webfluxdemo.domain.service.CalculatorService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class MultiplicationCalculator {

    private final ValidationService validationService;
    private final CalculatorService calculatorService;

    public MultiplicationCalculator(ValidationService validationService,
                                    CalculatorService calculatorService) {
        this.validationService = validationService;
        this.calculatorService = calculatorService;
    }

    public Flux<Long> calculateTable(Long number, Integer from, Integer to) {
        return Mono.fromSupplier(() -> new Range(from, to))
                .flatMap(this.validationService::validateRange)
                .flatMapMany(range -> this.calculatorService.calculateMultiplicationTable(number, range));
    }

    public Mono<Long> multiply(Multiplication multiplication) {
        return this.calculatorService.multiply(multiplication);
    }

}
