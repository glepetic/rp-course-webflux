package com.rp.webfluxdemo.infrastructure.configuration.spring;

import com.rp.webfluxdemo.adapter.driven.MultiplicationAdapter;
import com.rp.webfluxdemo.domain.port.MultiplicationRepository;
import com.rp.webfluxdemo.infrastructure.dto.entity.MultiplicationEntity;
import com.rp.webfluxdemo.infrastructure.repository.InMemoryMultiplicationRepository;
import com.rp.webfluxdemo.infrastructure.repository.dao.Dao;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RepositoryConfiguration {

    @Bean
    public MultiplicationRepository multiplicationRepository(Dao<MultiplicationEntity> dao,
                                                             MultiplicationAdapter adapter) {
        return new InMemoryMultiplicationRepository(dao, adapter);
    }

}
