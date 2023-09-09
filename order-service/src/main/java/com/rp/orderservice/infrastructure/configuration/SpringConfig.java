package com.rp.orderservice.infrastructure.configuration;

import com.rp.orderservice.adapter.ErrorAdapter;
import com.rp.orderservice.adapter.OrderAdapter;
import com.rp.orderservice.adapter.ProductAdapter;
import com.rp.orderservice.adapter.TransactionAdapter;
import com.rp.orderservice.application.usecase.OrderProcessor;
import com.rp.orderservice.application.usecase.OrderQuerier;
import com.rp.orderservice.domain.mapper.OrderMapper;
import com.rp.orderservice.domain.port.OrderPort;
import com.rp.orderservice.domain.port.ProductInfoPort;
import com.rp.orderservice.domain.port.UserPort;
import com.rp.orderservice.domain.service.OrderService;
import com.rp.orderservice.domain.service.ValidationService;
import com.rp.orderservice.infrastructure.client.ProductClient;
import com.rp.orderservice.infrastructure.client.UserClient;
import com.rp.orderservice.infrastructure.repository.OrderRDBRepository;
import com.rp.orderservice.infrastructure.repository.dao.OrderEntityDao;
import com.rp.orderservice.util.ExceptionUtil;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class SpringConfig {

    @Bean
    public ErrorAdapter errorAdapter() {
        return new ErrorAdapter();
    }

    @Bean
    public ProductAdapter productAdapter() {
        return new ProductAdapter();
    }

    @Bean
    public TransactionAdapter transactionAdapter() {
        return new TransactionAdapter();
    }

    @Bean
    public OrderAdapter orderAdapter() {
        return new OrderAdapter();
    }

    @Bean
    public ProductInfoPort productInfoPort(@Value("${com.rp.integrations.product.timeout.seconds}") int timeoutSeconds,
                                           @Value("${com.rp.integrations.product.retry.attempts}") int retryAttempts,
                                           @Value("${com.rp.integrations.product.retry.delay.seconds}") int retryFixedDelaySeconds,
                                           @Qualifier("productServiceWebClient") WebClient webClient,
                                           ProductAdapter productAdapter,
                                           ExceptionUtil exceptionUtil) {
        return new ProductClient(timeoutSeconds, retryAttempts, retryFixedDelaySeconds,
                webClient, productAdapter, exceptionUtil);
    }

    @Bean
    public UserPort transactionPort(@Value("${com.rp.integrations.user.transactions.uri}") String transactionsUri,
                                    @Value("${com.rp.integrations.user.timeout.seconds}") int timeoutSeconds,
                                    @Value("${com.rp.integrations.user.retry.attempts}") int retryAttempts,
                                    @Value("${com.rp.integrations.user.retry.delay.seconds}") int retryFixedDelaySeconds,
                                    @Qualifier("userServiceWebClient") WebClient webClient,
                                    TransactionAdapter transactionAdapter,
                                    ExceptionUtil exceptionUtil) {
        return new UserClient(transactionsUri, timeoutSeconds, retryAttempts, retryFixedDelaySeconds,
                webClient, transactionAdapter, exceptionUtil);
    }

    @Bean
    public OrderPort orderPort(OrderEntityDao dao,
                               OrderAdapter orderAdapter) {
        return new OrderRDBRepository(dao, orderAdapter);
    }

    @Bean
    public OrderService orderService(ValidationService validationService,
                                     ProductInfoPort productInfoPort,
                                     UserPort transactionPort,
                                     OrderPort orderPort,
                                     OrderMapper orderMapper) {
        return new OrderService(validationService, productInfoPort, transactionPort, orderPort, orderMapper);
    }

    @Bean
    public ValidationService validationService() {
        return new ValidationService();
    }

    @Bean
    public OrderMapper orderMapper() {
        return new OrderMapper();
    }

    @Bean
    public OrderProcessor orderProcessor(OrderService orderService,
                                         OrderAdapter orderAdapter) {
        return new OrderProcessor(orderService, orderAdapter);
    }

    @Bean
    public OrderQuerier orderQuerier(OrderService orderService,
                                     OrderAdapter orderAdapter) {
        return new OrderQuerier(orderService, orderAdapter);
    }

    @Bean
    public ExceptionUtil exceptionUtil() {
        return new ExceptionUtil();
    }

}
