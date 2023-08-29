package com.rp.webfluxdemo.infrastructure.dto.entity;

public record MultiplicationEntity(String id, long... numbers) implements EntityWithId {}
