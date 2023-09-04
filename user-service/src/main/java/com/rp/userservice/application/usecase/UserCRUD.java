package com.rp.userservice.application.usecase;

import com.rp.userservice.adapter.UserAdapter;
import com.rp.userservice.domain.model.User;
import com.rp.userservice.domain.service.UserService;
import com.rp.userservice.infrastructure.dto.request.UserRequest;
import com.rp.userservice.infrastructure.dto.response.UserResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.function.Supplier;

public class UserCRUD {

    private final UserService userService;
    private final UserAdapter userAdapter;

    public UserCRUD(UserService userService, UserAdapter userAdapter) {
        this.userService = userService;
        this.userAdapter = userAdapter;
    }

    public Flux<UserResponse> findAll() {
        return this.userService.findAll()
                .map(this.userAdapter::toResponse);
    }

    public Mono<UserResponse> findById(Long id) {
        return this.userService.findById(id)
                .map(this.userAdapter::toResponse);
    }

    public Mono<UserResponse> create(UserRequest userRequest) {
        return this.save(() -> this.userAdapter.toModel(userRequest));
    }

    public Mono<UserResponse> update(Long id, UserRequest userRequest) {
        return this.save(() -> this.userAdapter.toModel(id, userRequest));
    }

    private Mono<UserResponse> save(Supplier<User> userSupplier) {
        return Mono.fromSupplier(userSupplier)
                .flatMap(this.userService::save)
                .map(this.userAdapter::toResponse);
    }

    public Mono<Void> deleteById(Long id) {
        return this.userService.deleteById(id);
    }

}
