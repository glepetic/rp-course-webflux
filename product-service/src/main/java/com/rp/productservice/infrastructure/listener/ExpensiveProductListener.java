package com.rp.productservice.infrastructure.listener;

import com.mongodb.client.model.changestream.OperationType;
import com.rp.productservice.infrastructure.dto.entity.ProductEntity;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;

import java.util.Objects;

@Slf4j
public class ExpensiveProductListener {

    // This class needs the mongo to be a replica set to work

    private final ReactiveMongoTemplate mongoTemplate;

    public ExpensiveProductListener(ReactiveMongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

//    @PostConstruct
    public void listen() {
        this.mongoTemplate.changeStream(ProductEntity.class)
                .filter(Criteria.where("price").gte(1000))
                .listen()
                .filter(changeEvent -> Objects.equals(changeEvent.getOperationType(), OperationType.INSERT))
                // do something like send an email, or notify some service
                .doOnNext(changeEvent -> log.warn("New expensive product {}", changeEvent.getBody()))
                .subscribe();
    }

}
