package com.rp.orderservice.infrastructure.configuration;

import com.rp.orderservice.adapter.ErrorAdapter;
import com.rp.orderservice.adapter.OrderAdapter;
import com.rp.orderservice.adapter.ProductAdapter;
import com.rp.orderservice.adapter.TransactionAdapter;
import com.rp.orderservice.application.usecase.OrderProcessor;
import com.rp.orderservice.domain.port.OrderPort;
import com.rp.orderservice.domain.port.ProductInfoPort;
import com.rp.orderservice.domain.port.UserTransactionPort;
import com.rp.orderservice.domain.service.OrderService;
import com.rp.orderservice.infrastructure.client.ProductClient;
import com.rp.orderservice.infrastructure.client.UserTransactionClient;
import com.rp.orderservice.infrastructure.repository.OrderRDBRepository;
import com.rp.orderservice.infrastructure.repository.dao.OrderEntityDao;
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
    public ProductInfoPort productInfoPort(@Qualifier("productServiceWebClient") WebClient webClient,
                                           ProductAdapter productAdapter) {
        return new ProductClient(webClient, productAdapter);
    }

    @Bean
    public UserTransactionPort transactionPort(@Value("${com.rp.integrations.user.transactions.uri}") String transactionsUri,
                                               @Qualifier("userServiceWebClient") WebClient webClient,
                                               TransactionAdapter transactionAdapter) {
        return new UserTransactionClient(transactionsUri, webClient, transactionAdapter);
    }

    @Bean
    public OrderPort orderPort(OrderEntityDao dao,
                               OrderAdapter orderAdapter) {
        return new OrderRDBRepository(dao, orderAdapter);
    }

    @Bean
    public OrderService orderService(ProductInfoPort productInfoPort,
                                     UserTransactionPort transactionPort,
                                     OrderPort orderPort) {
        return new OrderService(productInfoPort, transactionPort, orderPort);
    }

    @Bean
    public OrderProcessor orderProcessor(OrderService orderService,
                                         OrderAdapter orderAdapter) {
        return new OrderProcessor(orderService, orderAdapter);
    }

}
