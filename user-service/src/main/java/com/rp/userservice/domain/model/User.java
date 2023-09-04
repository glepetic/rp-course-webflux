package com.rp.userservice.domain.model;

import lombok.Builder;

@Builder
public record User(Long id, String name, Integer accountBalance) {
}
