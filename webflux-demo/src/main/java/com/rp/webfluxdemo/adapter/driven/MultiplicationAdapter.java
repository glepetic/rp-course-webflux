package com.rp.webfluxdemo.adapter.driven;

import com.rp.webfluxdemo.domain.model.Multiplication;
import com.rp.webfluxdemo.infrastructure.dto.entity.MultiplicationEntity;

import java.util.Optional;
import java.util.UUID;

public class MultiplicationAdapter {

    public Multiplication to(MultiplicationEntity entity) {
        return new Multiplication(entity.id(), entity.numbers());
    }

    public MultiplicationEntity to(Multiplication model) {
        return new MultiplicationEntity(Optional.ofNullable(model.id()).orElseGet(UUID.randomUUID()::toString), model.numbers());
    }

}
