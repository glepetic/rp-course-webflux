package com.rp.productservice.adapter;

import com.rp.productservice.domain.exception.InvalidIdException;
import com.rp.productservice.domain.exception.InvalidRangeException;
import com.rp.productservice.domain.exception.ProductNotFoundException;
import com.rp.productservice.infrastructure.dto.response.ErrorCode;
import com.rp.productservice.infrastructure.dto.response.ErrorResponse;

import java.util.Map;

public class ErrorAdapter {

    private final Map<Class<? extends Throwable>, ErrorCode> codeMap = Map.of(
            ProductNotFoundException.class, ErrorCode.PRODUCT_NOT_FOUND,
            InvalidIdException.class, ErrorCode.INVALID_REQUEST,
            InvalidRangeException.class, ErrorCode.INVALID_REQUEST
    );

    public ErrorResponse toErrorResponse(Throwable throwable) {
        ErrorCode errorCode = this.getErrorCode(throwable);
        return new ErrorResponse(errorCode.getCode(), errorCode.name(), throwable.getMessage());
    }

    private ErrorCode getErrorCode(Throwable throwable) {
        return this.codeMap.get(throwable.getClass());
    }

}
