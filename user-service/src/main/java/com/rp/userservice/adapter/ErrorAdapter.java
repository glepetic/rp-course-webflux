package com.rp.userservice.adapter;

import com.rp.userservice.domain.exception.UserNotFoundException;
import com.rp.userservice.infrastructure.dto.response.ErrorCode;
import com.rp.userservice.infrastructure.dto.response.ErrorResponse;
import org.springframework.web.server.MissingRequestValueException;
import org.springframework.web.server.ServerWebInputException;

import java.util.Map;
import java.util.function.Supplier;

public class ErrorAdapter {

    private final Map<Class<? extends Throwable>, ErrorCode> codeMap = Map.of(
            UserNotFoundException.class, ErrorCode.USER_NOT_FOUND,
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
