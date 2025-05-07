package com.dongsan.api.support;

public record TokenInfo(
        String accessToken,
        String refreshToken
) {
}
