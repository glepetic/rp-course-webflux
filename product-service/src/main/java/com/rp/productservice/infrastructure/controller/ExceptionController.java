package com.rp.productservice.infrastructure.controller;

import com.rp.productservice.adapter.ErrorAdapter;
import com.rp.productservice.infrastructure.dto.response.ErrorResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.MissingRequestValueException;
import org.springframework.web.server.ServerWebInputException;

import java.util.function.Supplier;

@RestControllerAdvice
public class ExceptionController {

    private final ErrorAdapter errorAdapter;

    @Autowired
    public ExceptionController(ErrorAdapter errorAdapter) {
        this.errorAdapter = errorAdapter;
    }

    @ExceptionHandler(MissingRequestValueException.class)
    public ResponseEntity<ErrorResponse> handleMissingRequestValue(MissingRequestValueException e) {
        return this.buildErrorResponse(e, e::getReason);
    }

    @ExceptionHandler(ServerWebInputException.class)
    public ResponseEntity<ErrorResponse> handleWebInput(ServerWebInputException e) {
        return this.buildErrorResponse(e, () -> e.getMostSpecificCause().getMessage().split("\\n")[0]);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleAnyException(Exception e) {
        return this.buildErrorResponse(e, e::getMessage);
    }

    private ResponseEntity<ErrorResponse> buildErrorResponse(Throwable throwable, Supplier<String> messageProvider) {
        ErrorResponse errorResponse = this.errorAdapter.toErrorResponse(throwable, messageProvider);
        return ResponseEntity.status(errorResponse.status())
                .body(errorResponse);
    }

}
