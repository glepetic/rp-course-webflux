package com.rp.productservice.infrastructure.configuration;

import com.rp.productservice.adapter.ErrorAdapter;
import com.rp.productservice.adapter.ProductAdapter;
import com.rp.productservice.domain.port.ProductRepository;
import com.rp.productservice.domain.service.ProductService;
import com.rp.productservice.domain.service.ValidationService;
import com.rp.productservice.infrastructure.repository.ProductMongoRepository;
import com.rp.productservice.infrastructure.repository.dao.ProductEntityDao;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringConfig {

    @Bean
    public ProductService productService(ValidationService validationService,
                                         ProductAdapter productAdapter,
                                         ProductRepository productRepository) {
        return new ProductService(validationService, productAdapter, productRepository);
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

}
