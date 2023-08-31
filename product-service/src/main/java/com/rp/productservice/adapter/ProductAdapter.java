package com.rp.productservice.adapter;

import com.rp.productservice.domain.model.Product;
import com.rp.productservice.infrastructure.dto.entity.ProductEntity;
import com.rp.productservice.infrastructure.dto.request.ProductRequest;
import com.rp.productservice.infrastructure.dto.response.ProductResponse;
import org.bson.types.ObjectId;

public class ProductAdapter {

    public Product toModel(ProductRequest productRequest) {
        return this.toModel(ObjectId.get().toString(), productRequest);
    }

    public Product toModel(String id, ProductRequest productRequest) {
        return new Product(id, productRequest.detail(), productRequest.price());
    }

    public Product toModel(ProductEntity entity) {
        return new Product(entity.id().toString(), entity.description(), entity.price());
    }

    public ProductResponse toResponse(Product product) {
        return new ProductResponse(product.id(), product.description(), product.price());
    }

    public ProductEntity toEntity(Product product) {
        return new ProductEntity(new ObjectId(product.id()), product.description(), product.price());
    }
}
