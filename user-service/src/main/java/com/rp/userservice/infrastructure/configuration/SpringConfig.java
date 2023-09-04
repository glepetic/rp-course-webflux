package com.rp.userservice.infrastructure.configuration;

import com.rp.userservice.adapter.ErrorAdapter;
import com.rp.userservice.adapter.TransactionAdapter;
import com.rp.userservice.adapter.UserAdapter;
import com.rp.userservice.application.usecase.TransactionProcessing;
import com.rp.userservice.application.usecase.UserCRUD;
import com.rp.userservice.domain.port.UserRepository;
import com.rp.userservice.domain.port.UserTransactionRepository;
import com.rp.userservice.domain.service.TransactionService;
import com.rp.userservice.domain.service.UserService;
import com.rp.userservice.infrastructure.repository.TransactionRDBRepository;
import com.rp.userservice.infrastructure.repository.UserRDBRepository;
import com.rp.userservice.infrastructure.repository.dao.TransactionEntityDao;
import com.rp.userservice.infrastructure.repository.dao.UserEntityDao;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringConfig {

    @Bean
    public ErrorAdapter errorAdapter() {
        return new ErrorAdapter();
    }

    @Bean
    public UserAdapter userAdapter() {
        return new UserAdapter();
    }

    @Bean
    public TransactionAdapter transactionAdapter() {
        return new TransactionAdapter();
    }

    @Bean
    public UserService userService(UserRepository userRepository) {
        return new UserService(userRepository);
    }

    @Bean
    public TransactionService transactionService(UserTransactionRepository transactionRepository,
                                                 UserRepository userRepository) {
        return new TransactionService(transactionRepository, userRepository);
    }

    @Bean
    public UserRepository userRepository(UserEntityDao userEntityDao,
                                         UserAdapter userAdapter) {
        return new UserRDBRepository(userEntityDao, userAdapter);
    }

    @Bean
    public UserTransactionRepository userRepository(TransactionEntityDao transactionEntityDao,
                                                    TransactionAdapter transactionAdapter) {
        return new TransactionRDBRepository(transactionEntityDao, transactionAdapter);
    }

    @Bean
    public UserCRUD userCRUD(UserService userService,
                             UserAdapter userAdapter) {
        return new UserCRUD(userService, userAdapter);
    }

    @Bean
    public TransactionProcessing transactionCreate(TransactionService transactionService,
                                                   TransactionAdapter transactionAdapter) {
        return new TransactionProcessing(transactionService, transactionAdapter);
    }

}