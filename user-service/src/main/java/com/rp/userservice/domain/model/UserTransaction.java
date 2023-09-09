package com.rp.userservice.domain.model;

import java.time.LocalDateTime;

public record UserTransaction(Long userId, int amount, LocalDateTime datetime) {
}
