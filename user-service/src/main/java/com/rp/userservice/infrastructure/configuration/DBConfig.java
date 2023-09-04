package com.rp.userservice.infrastructure.configuration;

import com.rp.userservice.infrastructure.boot.DBSetup;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;

@Configuration
public class DBConfig {

    @Bean
    public DBSetup dbSetup(@Value("classpath:h2/init.sql") Resource initSql,
                           R2dbcEntityTemplate entityTemplate) {
        return new DBSetup(initSql, entityTemplate);
    }

}
