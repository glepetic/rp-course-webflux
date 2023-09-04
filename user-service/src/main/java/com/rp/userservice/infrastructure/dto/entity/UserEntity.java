package com.rp.userservice.infrastructure.dto.entity;

import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Builder
@Table("users")
public record UserEntity(@Id Long id,
                         String name,
                         Integer balance) {
}
