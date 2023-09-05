package com.rp.userservice.domain.service;

import com.rp.userservice.domain.model.User;
import com.rp.userservice.domain.port.UserPort;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class UserService {

    private final UserPort userPort;

    public UserService(UserPort userPort) {
        this.userPort = userPort;
    }

    public Flux<User> findAll() {
        return this.userPort.findAll();
    }

    public Mono<User> findById(long id) {
        return this.userPort.findById(id);
    }

    public Mono<User> insert(User user) {
        return this.userPort.save(user);
    }

    public Mono<User> update(User user) {
        return this.userPort.update(user);
    }

    public Mono<Void> deleteById(long id) {
        return this.userPort.deleteById(id);
    }

}
