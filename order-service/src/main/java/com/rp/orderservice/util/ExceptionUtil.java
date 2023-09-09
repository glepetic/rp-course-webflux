package com.rp.orderservice.util;

import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.Optional;
import java.util.concurrent.TimeoutException;
import java.util.function.Predicate;

public class ExceptionUtil {

    public Predicate<Throwable> isServer5xxError() {
        return throwable -> Optional.of(throwable)
                .filter(thr -> thr instanceof WebClientResponseException)
                .map(WebClientResponseException.class::cast)
                .filter(e -> e.getStatusCode().is5xxServerError())
                .isPresent();
    }

    public Predicate<Throwable> isTimeoutError() {
        return throwable -> Optional.of(throwable)
                .filter(thr -> thr instanceof TimeoutException)
                .isPresent();
    }

    public Predicate<Throwable> isWebRequestException() {
        return throwable -> Optional.of(throwable)
                .filter(thr -> thr instanceof WebClientRequestException)
                .isPresent();
    }

}
