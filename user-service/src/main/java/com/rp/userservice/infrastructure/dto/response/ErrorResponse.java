package com.rp.userservice.infrastructure.dto.response;

public record ErrorResponse(int status, int code, String name, String message) {
}
