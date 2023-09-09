package com.rp.orderservice.domain.exception;

import lombok.Getter;

@Getter
public class OrderProcessingException extends RuntimeException {

    private static final String TEMPLATE =
            "Reason: %s -> Explanation: %s -> Message: %s";

    private final String reason;
    private final String explanation;

    public OrderProcessingException(String reason,
                                    Throwable throwable) {
        super(throwable.getMessage(), throwable.getCause());
        this.reason = reason;
        this.explanation = throwable.getCause().getMessage();
    }

    public OrderProcessingException(String reason,
                                    String explanation,
                                    Throwable throwable) {
        super(throwable.getMessage(), throwable.getCause());
        this.reason = reason;
        this.explanation = explanation;
    }

    public String getFullDetail() {
        return String.format(TEMPLATE, this.reason, this.explanation, this.getMessage());
    }

}
