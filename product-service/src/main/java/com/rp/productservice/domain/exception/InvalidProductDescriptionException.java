package com.rp.productservice.domain.exception;

public class InvalidProductDescriptionException extends RuntimeException {

    private static final String TEMPLATE = "Products must have a non-null description with at least 4 characters of length: %s is invalid";

    public InvalidProductDescriptionException(String productDetail) {
        super(String.format(TEMPLATE, productDetail));
    }
}
