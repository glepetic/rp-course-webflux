package com.rp.productservice.infrastructure.repository.dao;

import com.rp.productservice.infrastructure.dto.entity.ProductEntity;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Range;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface ProductEntityDao extends ReactiveMongoRepository<ProductEntity, ObjectId> {

    // this is exclusive range
    // Flux<ProductEntity> findByPriceBetween(Integer minimum, Integer maximum);
    Flux<ProductEntity> findByPriceBetween(Range<Integer> range);

}
