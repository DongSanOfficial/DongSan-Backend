package com.dongsan.core.domains.auth;

import org.springframework.stereotype.Repository;

@Repository
public interface TokenRepository {
    public void saveRefreshToken(Long memberId, String refreshToken);

    public void deleteRefreshToken(Long memberId);

    public String getRefreshToken(Long memberId);

    public boolean compareRefreshToken(Long memberId, String refreshToken);

}
