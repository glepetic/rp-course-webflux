package com.rp.productservice.infrastructure.controller;

import com.rp.productservice.adapter.ErrorAdapter;
import com.rp.productservice.domain.exception.InvalidIdException;
import com.rp.productservice.domain.exception.InvalidRangeException;
import com.rp.productservice.domain.exception.ProductNotFoundException;
import com.rp.productservice.infrastructure.dto.response.ErrorResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionController {

    private final ErrorAdapter errorAdapter;

    @Autowired
    public ExceptionController(ErrorAdapter errorAdapter) {
        this.errorAdapter = errorAdapter;
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleProductNotFound(ProductNotFoundException e) {
        return this.buildErrorResponse(HttpStatus.NOT_FOUND.value(), e);
    }

    @ExceptionHandler(InvalidIdException.class)
    public ResponseEntity<ErrorResponse> handleInvalidId(InvalidIdException e) {
        return this.buildErrorResponse(HttpStatus.BAD_REQUEST.value(), e);
    }

    @ExceptionHandler(InvalidRangeException.class)
    public ResponseEntity<ErrorResponse> handleInvalidRange(InvalidRangeException e) {
        return this.buildErrorResponse(HttpStatus.BAD_REQUEST.value(), e);
    }

    private ResponseEntity<ErrorResponse> buildErrorResponse(int statusCode, Throwable throwable) {
        return ResponseEntity.status(statusCode)
                .body(this.errorAdapter.toErrorResponse(throwable));
    }

}
