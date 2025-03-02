package com.dongsan.api.support.response;

public record ValidationError(
        String field,
        String message) {
}
