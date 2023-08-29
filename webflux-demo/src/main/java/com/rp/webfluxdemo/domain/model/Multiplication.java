package com.rp.webfluxdemo.domain.model;

import lombok.Builder;

@Builder
public record Multiplication(String id, long... numbers) {}
