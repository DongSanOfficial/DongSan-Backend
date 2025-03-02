package com.dongsan.api.domains.auth;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class CookieService {

    @Value("${cookie.domain}")
    private String domain;

    @Value("${cookie.access-name}")
    private String accessTokenName;

    @Value("${cookie.refresh-name}")
    private String refreshTokenName;

    @Value("${cookie.at-max-age}")
    private int accessTokenMaxAge;

    @Value("${cookie.rt-max-age}")
    private int refreshTokenMaxAge;

    public Cookie createAccessTokenCookie(String token) {
        return createTokenCookie(accessTokenName, token, accessTokenMaxAge);
    }

    public Cookie createRefreshTokenCookie(String token) {
        return createTokenCookie(refreshTokenName, token, refreshTokenMaxAge);
    }

    public void deleteAllTokenCookie(HttpServletResponse response){
        response.addCookie(deleteAccessTokenCookie());
        response.addCookie(deleteRefreshTokenCookie());
    }

    public Cookie deleteAccessTokenCookie(){
        return createTokenCookie(accessTokenName, "", 0);
    }

    public Cookie deleteRefreshTokenCookie(){
        return createTokenCookie(refreshTokenName, "", 0);
    }

    private Cookie createTokenCookie(String cookieName, String token, int maxAge) {
        Cookie cookie = new Cookie(cookieName, token);
        cookie.setMaxAge(maxAge);
        cookie.setDomain(domain);
        cookie.setPath("/");
        cookie.setHttpOnly(false);

        // http 쿠키 전송을 위해
        cookie.setAttribute("SameSite", "None");
        cookie.setSecure(true);

        return cookie;
    }

    public String getAccessTokenFromCookie(HttpServletRequest request) {
        return getTokenFromCookie(request, accessTokenName);
    }

    public String getRefreshTokenFromCookie(HttpServletRequest request){
        return getTokenFromCookie(request, refreshTokenName);
    }

    private String getTokenFromCookie(HttpServletRequest request, String cookieName){
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if (cookie.getName().equals(cookieName)) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}
