package com.rp.webfluxdemo.infrastructure.dto.response;

import java.time.LocalDateTime;

public record ResultResponse(LocalDateTime date,
                             Long result) {}
