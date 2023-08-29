package com.rp.webfluxdemo.domain.exception;

public class InvalidRangeException extends RuntimeException {

    private static final String RANGE_TEMPLATE = "Cannot make a valid range with inputs (from,to) = (%s,%s)";
    private static final String INPUT_TEMPLATE = "Valid range is 10 to 20. Input %s is out of range";

    public InvalidRangeException(Integer from, Integer to) {
        super(String.format(RANGE_TEMPLATE, from, to));
    }

    public InvalidRangeException(Long input) {
        super(String.format(INPUT_TEMPLATE, input));
    }

}
