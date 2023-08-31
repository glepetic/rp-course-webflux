package com.rp.productservice.domain.exception;

public class InvalidProductDetailException extends RuntimeException {

    private static final String TEMPLATE = "Products must have a non-null detail with at least 4 characters of length: %s is invalid";

    public InvalidProductDetailException(String productDetail) {
        super(String.format(TEMPLATE, productDetail));
    }
}
