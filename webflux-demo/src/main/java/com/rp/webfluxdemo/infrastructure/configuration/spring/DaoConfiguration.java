package com.rp.webfluxdemo.infrastructure.configuration.spring;

import com.rp.webfluxdemo.infrastructure.dto.entity.MultiplicationEntity;
import com.rp.webfluxdemo.infrastructure.repository.dao.Dao;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DaoConfiguration {

    @Bean
    public Dao<MultiplicationEntity> multiplicationEntityDao() {
        return new Dao<>();
    }

}
