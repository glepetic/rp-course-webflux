package com.rp.userservice.adapter;

import com.rp.userservice.domain.model.User;
import com.rp.userservice.infrastructure.dto.entity.UserEntity;
import com.rp.userservice.infrastructure.dto.request.UserRequest;
import com.rp.userservice.infrastructure.dto.response.UserResponse;

public class UserAdapter {
    public UserResponse toResponse(User user) {
        return new UserResponse(user.id(), user.name(), user.accountBalance());
    }

    public User toModel(UserRequest userRequest) {
        return User.builder()
                .name(userRequest.name())
                .accountBalance(userRequest.balance())
                .build();
    }

    public User toModel(long id, UserRequest userRequest) {
        return new User(id, userRequest.name(), userRequest.balance());
    }

    public UserEntity toEntity(User user) {
        return UserEntity.builder()
                .id(user.id())
                .name(user.name())
                .balance(user.accountBalance())
                .build();
    }

    public User toModel(UserEntity entity) {
        return new User(entity.id(), entity.name(), entity.balance());
    }
}
