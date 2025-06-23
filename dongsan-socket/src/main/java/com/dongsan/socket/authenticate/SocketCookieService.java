package com.dongsan.socket.authenticate;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

@Service
public class SocketCookieService {

    private static final String ACCESS_TOKEN = "accessToken";

    public String getAccessTokenFromCookie(HttpServletRequest request) {
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if (cookie.getName()
                        .equals(SocketCookieService.ACCESS_TOKEN)) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

}
