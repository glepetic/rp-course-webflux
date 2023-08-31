package com.rp.productservice.infrastructure.dto.response;

import lombok.Getter;

public enum ErrorCode {
    UNKNOWN(-1, 500),
    PRODUCT_NOT_FOUND(1, 404),
    INVALID_REQUEST(2, 400),
    ;


    private final @Getter int code;
    private final @Getter int status;

    ErrorCode(int code, int status) {
        this.code = code;
        this.status = status;
    }

}
