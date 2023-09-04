package com.rp.userservice.domain.model;

import java.time.LocalDateTime;

public record UserTransaction(long userId, int amount, LocalDateTime datetime) {
}
