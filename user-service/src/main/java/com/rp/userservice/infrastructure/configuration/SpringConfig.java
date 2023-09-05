package com.rp.userservice.infrastructure.configuration;

import com.rp.userservice.adapter.ErrorAdapter;
import com.rp.userservice.adapter.TransactionAdapter;
import com.rp.userservice.adapter.UserAdapter;
import com.rp.userservice.application.usecase.TransactionProcessor;
import com.rp.userservice.application.usecase.UserCRUD;
import com.rp.userservice.domain.port.UserPort;
import com.rp.userservice.domain.port.UserTransactionPort;
import com.rp.userservice.domain.service.TransactionService;
import com.rp.userservice.domain.service.UserService;
import com.rp.userservice.domain.service.ValidationService;
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
    public UserService userService(UserPort userPort) {
        return new UserService(userPort);
    }

    @Bean
    public TransactionService transactionService(ValidationService validationService,
                                                 UserTransactionPort transactionRepository,
                                                 UserPort userPort) {
        return new TransactionService(validationService, transactionRepository, userPort);
    }

    @Bean
    public ValidationService validationService() {
        return new ValidationService();
    }

    @Bean
    public UserPort userRepository(UserEntityDao userEntityDao,
                                   UserAdapter userAdapter) {
        return new UserRDBRepository(userEntityDao, userAdapter);
    }

    @Bean
    public UserTransactionPort transactionRepository(TransactionEntityDao transactionEntityDao,
                                                     TransactionAdapter transactionAdapter) {
        return new TransactionRDBRepository(transactionEntityDao, transactionAdapter);
    }

    @Bean
    public UserCRUD userCRUD(UserService userService,
                             UserAdapter userAdapter) {
        return new UserCRUD(userService, userAdapter);
    }

    @Bean
    public TransactionProcessor transactionCreate(TransactionService transactionService,
                                                  TransactionAdapter transactionAdapter) {
        return new TransactionProcessor(transactionService, transactionAdapter);
    }

}
