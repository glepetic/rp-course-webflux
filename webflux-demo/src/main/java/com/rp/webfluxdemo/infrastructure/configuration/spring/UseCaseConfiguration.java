package com.rp.webfluxdemo.infrastructure.configuration.spring;

import com.rp.webfluxdemo.application.service.ValidationService;
import com.rp.webfluxdemo.application.usecase.SquareCalculator;
import com.rp.webfluxdemo.application.usecase.MultiplicationCalculator;
import com.rp.webfluxdemo.domain.service.CalculatorService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfiguration {

    @Bean
    public SquareCalculator squareCalculator(CalculatorService calculatorService) {
        return new SquareCalculator(calculatorService);
    }

    @Bean
    public MultiplicationCalculator tableCalculator(ValidationService validationService,
                                                    CalculatorService calculatorService) {
        return new MultiplicationCalculator(validationService, calculatorService);
    }

}
