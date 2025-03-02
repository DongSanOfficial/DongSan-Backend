package com.dongsan.api.domains.auth;

import com.dongsan.api.support.error.ApiErrorCode;
import com.dongsan.api.support.error.ApiException;
import com.dongsan.core.domains.auth.GetTokenRemaining;
import com.dongsan.core.domains.auth.TokenReader;
import com.dongsan.core.domains.auth.TokenWriter;
import com.dongsan.core.domains.member.Member;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

@Component
public class AuthService {
    private final TokenWriter tokenWriter;
    private final TokenReader tokenReader;
    private final JwtService jwtService;
    private final CookieService cookieService;

    public AuthService(TokenWriter tokenWriter, TokenReader tokenReader, JwtService jwtService, CookieService cookieService) {
        this.tokenWriter = tokenWriter;
        this.tokenReader = tokenReader;
        this.jwtService = jwtService;
        this.cookieService = cookieService;
    }

    public void logout(Long memberId, HttpServletResponse response) {
        cookieService.deleteAllTokenCookie(response);
        tokenWriter.deleteRefreshToken(memberId);
    }

    /**
     * access token 만료 시 refresh token 을 보내서, access token & refresh token 둘다 갱신 만약 refresh token 기간 만료됨 -> 다시 로그인 시도
     * 하라고 에러 반환 만약 refresh token 기간 만료 안됨 -> Id(Key) 확인 후 value 와 동일한 토큰 인지 확인 (갱신 전 토큰 일 수도) -> value 와 다른 토큰 : 다시 로그인
     * 시도 하라고 에러 반환 -> value 와 동일한 토큰 : 토큰 갱신
     *
     * @param request
     * @param response
     * @return 새로 갱신한 access token & refresh token
     */
    public void renewToken(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = cookieService.getRefreshTokenFromCookie(request);
        if (!jwtService.isRefreshTokenExpired(refreshToken)) {
            Member member = jwtService.getMemberFromRefreshToken(refreshToken);
            if (tokenReader.isRefreshTokenNotReplaced(member.id(), refreshToken)) {
                String newAccessToken = jwtService.createAccessToken(member.id());
                String newRefreshToken = jwtService.createRefreshToken(member.id());
                response.addCookie(cookieService.createAccessTokenCookie(newAccessToken));
                response.addCookie(cookieService.createRefreshTokenCookie(newRefreshToken));
                tokenWriter.saveRefreshToken(member.id(), newRefreshToken);
            }
            else{
                cookieService.deleteAllTokenCookie(response);
                throw new ApiException(ApiErrorCode.AUTHENTICATION_FAILED);
            }
        }
        else{
            cookieService.deleteAllTokenCookie(response);
            throw new ApiException(ApiErrorCode.AUTHENTICATION_FAILED);
        }
    }

    /**
     * 토큰의 만료기간을 확인하는 로직
     */
    public GetTokenRemaining checkTokenExpire(HttpServletRequest request) {
        String accessToken = cookieService.getAccessTokenFromCookie(request);
        String refreshToken = cookieService.getRefreshTokenFromCookie(request);
        long accessTokenRemaining = accessToken == null ? 0 : jwtService.getAccessTokenRemainingTimeMillis(accessToken);
        long refreshTokenRemaining = refreshToken == null ? 0 : jwtService.getRefreshTokenRemainingTimeMillis(refreshToken);
        if(refreshTokenRemaining > 0){
            Long memberId = jwtService.getMemberFromRefreshToken(refreshToken).id();
            if(!tokenReader.isRefreshTokenNotReplaced(memberId, refreshToken)){
                refreshTokenRemaining = 0;
            }
        }
        return new GetTokenRemaining(accessTokenRemaining, refreshTokenRemaining);
    }

}
