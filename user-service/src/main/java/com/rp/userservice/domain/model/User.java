package com.rp.userservice.domain.model;

import lombok.Builder;

@Builder
public record User(long id, String name, int accountBalance) {
}
