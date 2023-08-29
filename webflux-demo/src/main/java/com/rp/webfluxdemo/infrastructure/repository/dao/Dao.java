package com.rp.webfluxdemo.infrastructure.repository.dao;

import com.rp.webfluxdemo.infrastructure.dto.entity.EntityWithId;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

public class Dao<T extends EntityWithId> {

    protected final Map<String, T> db;

    public Dao() {
        this.db = new HashMap<>();
    }

    public Mono<T> insert(T element) {
        return Mono.fromSupplier(() -> {
            this.db.put(element.id(), element);
            return element;
        });
    }
    public Mono<T> get(String id) {
        return Mono.justOrEmpty(this.db.get(id));
    }

}
