package com.dongsan.api.support;

import com.dongsan.api.domains.auth.JwtService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

@Component
public class TestAuthHelper {
    private final JwtService jwtService;

    public TestAuthHelper(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    public HttpHeaders generateTokenHeader(Long memberId) {
        TokenInfo tokenInfo = generateToken(memberId);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Cookie", "accessToken=" + tokenInfo.accessToken());
        headers.add("Cookie", "refreshToken=" + tokenInfo.refreshToken());
        return headers;
    }

    private TokenInfo generateToken(Long memberId) {
        String accessToken = jwtService.createAccessToken(memberId);
        String refreshToken = jwtService.createRefreshToken(memberId);
        return new TokenInfo(accessToken, refreshToken);
    }
}
