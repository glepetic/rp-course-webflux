package com.rp.productservice.domain.exception;

public class ProductNotFoundException extends RuntimeException {
    private static final String TEMPLATE = "Could not find a product with id: %s";
    public ProductNotFoundException(String id) {
        super(String.format(TEMPLATE, id));
    }
}
