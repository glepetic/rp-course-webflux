package com.rp.webfluxdemo.infrastructure.controller;

import com.rp.webfluxdemo.domain.exception.MultiplicationDoesNotExistException;
import com.rp.webfluxdemo.infrastructure.dto.response.ErrorResponse;
import com.rp.webfluxdemo.domain.exception.InvalidRangeException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionController {

    @ExceptionHandler(InvalidRangeException.class)
    public HttpEntity<ErrorResponse> handleInvalidRangeException(InvalidRangeException e) {
        return this.buildResponseEntity(100, e, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MultiplicationDoesNotExistException.class)
    public HttpEntity<ErrorResponse> handleMultiplicationDoesNotExistException(MultiplicationDoesNotExistException e) {
        return this.buildResponseEntity(101, e, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public HttpEntity<ErrorResponse> handleGenericException(Exception e) {
        return this.buildResponseEntity(-1, e, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private HttpEntity<ErrorResponse> buildResponseEntity(int code, Exception e, HttpStatus status) {
        return ResponseEntity
                .status(status)
                .body(this.buildErrorResponse(code, e));
    }

    private ErrorResponse buildErrorResponse(int code, Exception e) {
        return new ErrorResponse(code, e.getMessage());
    }

}
