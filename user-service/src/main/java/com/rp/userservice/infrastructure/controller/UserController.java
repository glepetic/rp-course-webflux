package com.rp.userservice.infrastructure.controller;

import com.rp.userservice.application.usecase.UserCRUD;
import com.rp.userservice.infrastructure.dto.request.UserRequest;
import com.rp.userservice.infrastructure.dto.response.UserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserCRUD userCRUD;

    @Autowired
    public UserController(UserCRUD userCRUD) {
        this.userCRUD = userCRUD;
    }

    @GetMapping
    public Flux<UserResponse> findAll() {
        return this.userCRUD.findAll();
    }

    @GetMapping("/{id}")
    public Mono<UserResponse> findById(@PathVariable Long id) {
        return this.userCRUD.findById(id);
    }

    @PostMapping
    public Mono<ResponseEntity<UserResponse>> create(@RequestBody Mono<UserRequest> userRequestMono) {
        return userRequestMono
                .flatMap(this.userCRUD::create)
                .map(user -> ResponseEntity.status(HttpStatus.CREATED)
                        .body(user));
    }

    @PutMapping("/{id}")
    public Mono<UserResponse> update(@PathVariable Long id,
                                     @RequestBody Mono<UserRequest> userRequestMono) {
        return userRequestMono
                .flatMap(userRequest -> this.userCRUD.update(id, userRequest));
    }

    @DeleteMapping("/{id}")
    public Mono<Void> deleteById(@PathVariable Long id) {
        return this.userCRUD.deleteById(id);
    }

}
