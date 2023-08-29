package com.rp.webfluxdemo.infrastructure.configuration.spring;

import com.rp.webfluxdemo.adapter.driver.ResultAdapter;
import com.rp.webfluxdemo.application.service.ValidationService;
import com.rp.webfluxdemo.application.usecase.SquareCalculator;
import com.rp.webfluxdemo.application.usecase.MultiplicationCalculator;
import com.rp.webfluxdemo.domain.port.MultiplicationRepository;
import com.rp.webfluxdemo.domain.service.CalculatorService;
import com.rp.webfluxdemo.infrastructure.service.MathService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServiceConfiguration {

    /*
    DOMAIN
     */

    @Bean
    public CalculatorService calculatorService() {
        return new CalculatorService();
    }

    /*
    APPLICATION
     */

    @Bean
    public ValidationService validationService() {
        return new ValidationService();
    }

    /*
    INFRASTRUCTURE
     */

    @Bean
    public MathService mathService(SquareCalculator squareCalculator,
                                   MultiplicationCalculator multiplicationCalculator,
                                   MultiplicationRepository multiplicationRepository,
                                   ResultAdapter resultAdapter) {
        return new MathService(squareCalculator, multiplicationCalculator, multiplicationRepository, resultAdapter);
    }

}
