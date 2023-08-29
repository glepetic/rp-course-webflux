package com.rp.webfluxdemo.webclient.testUtil;

public class ExternalApiException extends RuntimeException {

    private static final String TEMPLATE = "Could not complete request to external API. Body was: %s";
    public ExternalApiException(String body) {
        super(String.format(TEMPLATE, body));
    }
}
