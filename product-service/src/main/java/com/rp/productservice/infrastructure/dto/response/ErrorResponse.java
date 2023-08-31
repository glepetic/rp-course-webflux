package com.rp.productservice.infrastructure.dto.response;

public record ErrorResponse(int code, String description, String detail) {}
