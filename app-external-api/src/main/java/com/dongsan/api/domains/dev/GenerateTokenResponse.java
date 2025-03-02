package com.dongsan.api.domains.dev;

public record GenerateTokenResponse(
        String accessToken,
        String refreshToken
){ }
