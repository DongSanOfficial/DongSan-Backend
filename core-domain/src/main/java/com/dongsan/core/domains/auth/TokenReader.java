package com.dongsan.core.domains.auth;

import org.springframework.stereotype.Component;

@Component
public class TokenReader {
    private final TokenRepository tokenRepository;
    public TokenReader(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    /**
     * 현재 value(refresh token) 과 비교할 refresh token이 동일한지 확인
     * @param memberId
     * @param refreshToken
     * @return 동일하면 true, 아니면 false, 키가 없으면 false
     */
    public boolean isRefreshTokenNotReplaced(Long memberId, String refreshToken){
        return tokenRepository.compareRefreshToken(memberId, refreshToken);
    }

    /**
     * 서버에 저장된 memberId의 refresh token 조회
     * @param memberId  key
     * @return  value (refresh token, 없으면 Null)
     */
    public String getRefreshToken(Long memberId){
        return tokenRepository.getRefreshToken(memberId);
    }
}
