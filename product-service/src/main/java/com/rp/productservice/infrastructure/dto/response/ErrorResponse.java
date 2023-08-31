package com.rp.productservice.infrastructure.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;

public record ErrorResponse(@JsonIgnore int status,
                            int code,
                            String description,
                            String detail) {}
