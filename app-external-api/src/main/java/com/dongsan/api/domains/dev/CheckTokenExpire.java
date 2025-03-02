package com.dongsan.api.domains.dev;

public record CheckTokenExpire(
        String accessToken,
        String refreshToken
) {
}
