package com.rp.productservice.infrastructure.repository;

import com.rp.productservice.adapter.ProductAdapter;
import com.rp.productservice.domain.exception.ProductNotFoundException;
import com.rp.productservice.domain.model.Product;
import com.rp.productservice.domain.model.InclusiveRange;
import com.rp.productservice.domain.port.ProductRepository;
import com.rp.productservice.infrastructure.repository.dao.ProductEntityDao;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Range;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class ProductMongoRepository implements ProductRepository {

    private final ProductAdapter productAdapter;
    private final ProductEntityDao productDao;

    public ProductMongoRepository(ProductAdapter productAdapter, ProductEntityDao productDao) {
        this.productAdapter = productAdapter;
        this.productDao = productDao;
    }

    @Override
    public Mono<Product> findById(String id) {
        return Mono.just(id)
                .map(ObjectId::new)
                .flatMap(this.productDao::findById)
                .map(this.productAdapter::toModel)
                .switchIfEmpty(Mono.error(() -> new ProductNotFoundException(id)));
    }

    @Override
    public Flux<Product> findAll() {
        return this.productDao.findAll()
                .map(this.productAdapter::toModel);
    }

    @Override
    public Flux<Product> findInRange(InclusiveRange range) {
        return Mono.just(range)
                .map(r -> Range.closed(r.min(), r.max()))
                .flatMapMany(this.productDao::findByPriceBetween)
                .map(this.productAdapter::toModel);
    }

    @Override
    public Mono<Product> create(Product product) {
        return Mono.just(product)
                .map(this.productAdapter::toEntity)
                .flatMap(this.productDao::insert)
                .map(this.productAdapter::toModel);
    }

    @Override
    public Mono<Product> update(Product product) {
        return Mono.just(product)
                .map(this.productAdapter::toEntity)
                .flatMap(this.productDao::save)
                .map(this.productAdapter::toModel)
                .switchIfEmpty(Mono.error(() -> new ProductNotFoundException(product.id())));
    }

    @Override
    public Mono<Void> deleteById(String id) {
        return Mono.just(id)
                .map(ObjectId::new)
                .flatMap(this.productDao::deleteById)
                .switchIfEmpty(Mono.error(() -> new ProductNotFoundException(id)));
    }

}
