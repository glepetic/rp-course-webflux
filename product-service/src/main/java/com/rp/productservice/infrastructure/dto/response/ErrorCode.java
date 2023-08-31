package com.rp.productservice.infrastructure.dto.response;

import lombok.Getter;

public enum ErrorCode {
    PRODUCT_NOT_FOUND(1),
    INVALID_REQUEST(2);

    private final @Getter int code;

    ErrorCode(int code) {
        this.code = code;
    }

}
