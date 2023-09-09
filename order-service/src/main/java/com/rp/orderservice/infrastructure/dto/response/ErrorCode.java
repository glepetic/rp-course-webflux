package com.rp.orderservice.infrastructure.dto.response;

import lombok.Getter;

public enum ErrorCode {
    UNKNOWN("N/A", 500),
    INVALID_REQUEST("A1", 400),
    UNAVAILABLE_SERVICE("B1", 503)
    ;


    private final @Getter String code;
    private final @Getter int status;

    ErrorCode(String code, int status) {
        this.code = code;
        this.status = status;
    }

}
