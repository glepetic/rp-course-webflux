package com.rp.userservice.domain.exception;

public class UserNotFoundException extends RuntimeException {

    private static final String TEMPLATE = "Cannot find user with id %d";

    public UserNotFoundException(long id) {
        super(String.format(TEMPLATE, id));
    }

}
