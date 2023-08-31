package com.rp.productservice.domain.exception;

import com.rp.productservice.domain.model.Range;

public class InvalidRangeException extends Throwable {

    private static final String TEMPLATE = "Cannot construct valid increasing range (%d -> %d)";

    public InvalidRangeException(Range range) {
        super(String.format(TEMPLATE, range.min(), range.max()));
    }
}
