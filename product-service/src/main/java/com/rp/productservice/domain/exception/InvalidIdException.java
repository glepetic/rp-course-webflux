package com.rp.productservice.domain.exception;

public class InvalidIdException extends RuntimeException {
    private static final String TEMPLATE = "The id %s provided does not match the required pattern";
    public InvalidIdException(String id) {
        super(String.format(TEMPLATE, id));
    }
}
