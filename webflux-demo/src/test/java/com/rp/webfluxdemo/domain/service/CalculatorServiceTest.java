package com.rp.webfluxdemo.domain.service;

import com.rp.webfluxdemo.domain.model.Multiplication;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class CalculatorServiceTest {

    @InjectMocks
    private CalculatorService calculatorService;

    @Test
    public void multiplyTest() {

        // Assemble
        String someId = UUID.randomUUID().toString();
        Multiplication input = new Multiplication(someId, 5, 10, 2, -7);

        // Act
        Mono<Long> result = this.calculatorService.multiply(input);

        // Assert
        StepVerifier.create(result)
                .assertNext(output -> Assertions.assertEquals(-700, output))
                .verifyComplete();

    }

}
