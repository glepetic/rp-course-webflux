package com.rp.orderservice.infrastructure.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;

public record ErrorResponse(@JsonIgnore int status,
                            String code,
                            String description,
                            String detail) {}
