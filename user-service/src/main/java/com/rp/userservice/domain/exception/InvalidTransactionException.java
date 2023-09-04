package com.rp.userservice.domain.exception;

import lombok.Getter;

@Getter
public class InvalidTransactionException extends RuntimeException {

    private final String reason;

    public InvalidTransactionException(String message, String reason) {
        super(message);
        this.reason = reason;
    }

}
