package com.rp.userservice.domain.service;

import com.rp.userservice.domain.model.User;
import com.rp.userservice.domain.port.UserRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Flux<User> findAll() {
        return this.userRepository.findAll();
    }

    public Mono<User> findById(long id) {
        return this.userRepository.findById(id);
    }

    public Mono<User> save(User user) {
        return this.userRepository.save(user);
    }

    public Mono<Void> deleteById(long id) {
        return this.userRepository.deleteById(id);
    }

}
