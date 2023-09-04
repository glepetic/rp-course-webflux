package com.rp.userservice.infrastructure.dto.response;

import lombok.Getter;

public enum ErrorCode {
    UNKNOWN(0, 500),
    USER_NOT_FOUND(100, 404),
    INVALID_REQUEST(200, 400),
    ;


    private final @Getter int code;
    private final @Getter int status;

    ErrorCode(int code, int status) {
        this.code = code;
        this.status = status;
    }

}
