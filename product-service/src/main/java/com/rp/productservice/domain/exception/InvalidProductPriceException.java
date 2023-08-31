package com.rp.productservice.domain.exception;

public class InvalidProductPriceException extends RuntimeException {

    private static final String TEMPLATE = "Products cannot have price attribute with value: %s";

    public InvalidProductPriceException(int price) {
        super(String.format(TEMPLATE, price));
    }

}
