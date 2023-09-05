package com.rp.orderservice.adapter;

import com.rp.orderservice.domain.model.ProductInfo;
import com.rp.orderservice.infrastructure.dto.integration.product.ProductDto;

public class ProductAdapter {

    public ProductInfo toModel(ProductDto productDto) {
        return new ProductInfo(productDto.detail(), productDto.price());
    }

}
