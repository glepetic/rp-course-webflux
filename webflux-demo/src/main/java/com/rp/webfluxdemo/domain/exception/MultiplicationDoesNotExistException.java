package com.rp.webfluxdemo.domain.exception;

public class MultiplicationDoesNotExistException extends RuntimeException {

    public MultiplicationDoesNotExistException(String id) {
        super("Cannot find multiplication with id " + id);
    }
}
