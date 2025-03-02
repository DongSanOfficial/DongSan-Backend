package com.dongsan.core.domains.auth;

import org.springframework.stereotype.Component;

@Component
public class TokenWriter {
    private final TokenRepository tokenRepository;

    public TokenWriter(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    /**
     * refresh token 저장
     * @param memberId      key
     * @param refreshToken  value
     */
    public void saveRefreshToken(Long memberId, String refreshToken) {
        tokenRepository.saveRefreshToken(memberId, refreshToken);
    }

    public void deleteRefreshToken(Long memberId) {
        tokenRepository.deleteRefreshToken(memberId);
    }
}
