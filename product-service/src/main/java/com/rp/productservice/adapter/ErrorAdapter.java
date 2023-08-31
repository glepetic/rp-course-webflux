package com.rp.productservice.adapter;

import com.rp.productservice.domain.exception.*;
import com.rp.productservice.infrastructure.dto.response.ErrorCode;
import com.rp.productservice.infrastructure.dto.response.ErrorResponse;
import org.springframework.web.server.MissingRequestValueException;
import org.springframework.web.server.ServerWebInputException;

import java.util.Map;
import java.util.function.Supplier;

public class ErrorAdapter {

    private final Map<Class<? extends Throwable>, ErrorCode> codeMap = Map.of(
            ProductNotFoundException.class, ErrorCode.PRODUCT_NOT_FOUND,
            InvalidIdException.class, ErrorCode.INVALID_REQUEST,
            InvalidRangeException.class, ErrorCode.INVALID_REQUEST,
            MissingRequestValueException.class, ErrorCode.INVALID_REQUEST,
            ServerWebInputException.class, ErrorCode.INVALID_REQUEST,
            InvalidProductPriceException.class, ErrorCode.INVALID_REQUEST,
            InvalidProductDetailException.class, ErrorCode.INVALID_REQUEST
    );

    public ErrorResponse toErrorResponse(Throwable throwable, Supplier<String> messageProvider) {
        ErrorCode errorCode = this.getErrorCode(throwable);
        return new ErrorResponse(errorCode.getStatus(), errorCode.getCode(), errorCode.name(), messageProvider.get());
    }

    private ErrorCode getErrorCode(Throwable throwable) {
        return this.codeMap.getOrDefault(throwable.getClass(), ErrorCode.UNKNOWN);
    }

}
