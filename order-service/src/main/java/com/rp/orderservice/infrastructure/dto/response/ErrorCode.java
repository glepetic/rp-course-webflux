package com.rp.orderservice.infrastructure.dto.response;

import lombok.Getter;

public enum ErrorCode {
    UNKNOWN("N/A", 500),
    PRODUCT_NOT_FOUND("A1", 404),
    INVALID_REQUEST("A2", 400),
    ;


    private final @Getter String code;
    private final @Getter int status;

    ErrorCode(String code, int status) {
        this.code = code;
        this.status = status;
    }

}
