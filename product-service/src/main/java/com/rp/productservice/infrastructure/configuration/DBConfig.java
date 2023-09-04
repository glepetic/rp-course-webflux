package com.rp.productservice.infrastructure.configuration;

import com.rp.productservice.domain.service.ProductService;
import com.rp.productservice.infrastructure.boot.DBSetup;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DBConfig {

    @Bean
    public DBSetup dbSetup(ProductService productService) {
        return new DBSetup(productService);
    }

}
