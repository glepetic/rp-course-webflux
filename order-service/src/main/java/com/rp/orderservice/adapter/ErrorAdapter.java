package com.rp.orderservice.adapter;

import com.rp.orderservice.infrastructure.dto.response.ErrorCode;
import com.rp.orderservice.infrastructure.dto.response.ErrorResponse;
import org.springframework.web.server.MissingRequestValueException;
import org.springframework.web.server.ServerWebInputException;

import java.util.Map;
import java.util.function.Supplier;

public class ErrorAdapter {

    private final Map<Class<? extends Throwable>, ErrorCode> codeMap = Map.of(
            MissingRequestValueException.class, ErrorCode.INVALID_REQUEST,
            ServerWebInputException.class, ErrorCode.INVALID_REQUEST
    );

    public ErrorResponse toErrorResponse(Throwable throwable, Supplier<String> messageProvider) {
        ErrorCode errorCode = this.getErrorCode(throwable);
        return new ErrorResponse(errorCode.getStatus(), errorCode.getCode(), errorCode.name(), messageProvider.get());
    }

    private ErrorCode getErrorCode(Throwable throwable) {
        return this.codeMap.getOrDefault(throwable.getClass(), ErrorCode.UNKNOWN);
    }

}
