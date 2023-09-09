package com.rp.productservice.infrastructure.configuration;

import com.rp.productservice.infrastructure.dto.response.ProductResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

@Configuration
public class SinkConfig {

    @Bean
    public Sinks.Many<ProductResponse> sink() {
        return Sinks.many()
                .replay()
                .limit(1);
    }

    @Bean
    public Flux<ProductResponse> productBroadcast(Sinks.Many<ProductResponse> sink) {
        return sink.asFlux();
    }

}
