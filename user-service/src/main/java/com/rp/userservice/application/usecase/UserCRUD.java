package com.rp.userservice.application.usecase;

import com.rp.userservice.adapter.UserAdapter;
import com.rp.userservice.domain.service.UserService;
import com.rp.userservice.infrastructure.dto.request.UserRequest;
import com.rp.userservice.infrastructure.dto.response.UserResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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

    public Mono<UserResponse> findById(long id) {
        return this.userService.findById(id)
                .map(this.userAdapter::toResponse);
    }

    public Mono<UserResponse> create(UserRequest userRequest) {
        return Mono.just(userRequest)
                .map(this.userAdapter::toModel)
                .flatMap(this.userService::insert)
                .map(this.userAdapter::toResponse);
    }

    public Mono<UserResponse> update(long id, UserRequest userRequest) {
        return Mono.fromSupplier(() -> this.userAdapter.toModel(id, userRequest))
                .flatMap(this.userService::update)
                .map(this.userAdapter::toResponse);
    }

    public Mono<Void> deleteById(long id) {
        return this.userService.deleteById(id);
    }

}
