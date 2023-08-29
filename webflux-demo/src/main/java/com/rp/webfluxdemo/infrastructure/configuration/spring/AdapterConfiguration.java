package com.rp.webfluxdemo.infrastructure.configuration.spring;

import com.rp.webfluxdemo.adapter.driven.MultiplicationAdapter;
import com.rp.webfluxdemo.adapter.driver.ResultAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AdapterConfiguration {

    /*
    DRIVER
     */

    @Bean
    public ResultAdapter resultAdapter() {
        return new ResultAdapter();
    }

    /*
    DRIVEN
     */

    @Bean
    public MultiplicationAdapter multiplicationAdapter() {
        return new MultiplicationAdapter();
    }

}
