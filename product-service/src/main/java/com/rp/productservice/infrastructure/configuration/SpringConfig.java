package com.rp.productservice.infrastructure.configuration;

import com.rp.productservice.adapter.ErrorAdapter;
import com.rp.productservice.adapter.ProductAdapter;
import com.rp.productservice.application.usecase.ProductCRUD;
import com.rp.productservice.domain.port.ProductRepository;
import com.rp.productservice.domain.service.ProductService;
import com.rp.productservice.domain.service.ValidationService;
import com.rp.productservice.infrastructure.listener.ExpensiveProductListener;
import com.rp.productservice.infrastructure.repository.ProductMongoRepository;
import com.rp.productservice.infrastructure.repository.dao.ProductEntityDao;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;

@Configuration
public class SpringConfig {

    @Bean
    public ProductService productService(ValidationService validationService,
                                         ProductRepository productRepository) {
        return new ProductService(validationService, productRepository);
    }

    @Bean
    public ValidationService validationService() {
        return new ValidationService();
    }

    @Bean
    public ProductAdapter productAdapter() {
        return new ProductAdapter();
    }

    @Bean
    public ErrorAdapter errorAdapter() {
        return new ErrorAdapter();
    }

    @Bean
    public ProductRepository productMongoRepository(ProductAdapter productAdapter,
                                                    ProductEntityDao productDao) {
        return new ProductMongoRepository(productAdapter, productDao);
    }

    @Bean
    public ProductCRUD productCRUD(ProductService productService,
                                   ProductAdapter productAdapter) {
        return new ProductCRUD(productService, productAdapter);
    }

    @Bean
    public ExpensiveProductListener expensiveProductListener(ReactiveMongoTemplate mongoTemplate) {
        return new ExpensiveProductListener(mongoTemplate);
    }

}
