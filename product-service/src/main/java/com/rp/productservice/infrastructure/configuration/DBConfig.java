package com.rp.productservice.infrastructure.configuration;

import com.rp.productservice.application.usecase.ProductCRUD;
import com.rp.productservice.infrastructure.boot.DBSetup;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DBConfig {

    @Bean
    public DBSetup dbSetup(ProductCRUD productCRUD) {
        return new DBSetup(productCRUD);
    }

}
