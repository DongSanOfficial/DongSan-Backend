package com.dongsan.api.domains.auth;

import com.dongsan.domain.domains.refresh.TokenWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private static final Logger log = LoggerFactory.getLogger(CustomSuccessHandler.class);
    private final JwtService jwtService;
    private final CookieService cookieService;
    private final TokenWriter authWriter;

    @Value("${frontend.dev-redirect-url}")
    private String devRedirectUrl;

    @Value("${frontend.prod-redirect-url}")
    private String prodRedirectUrl;

    public CustomSuccessHandler(JwtService jwtService, CookieService cookieService, TokenWriter authWriter) {
        this.jwtService = jwtService;
        this.cookieService = cookieService;
        this.authWriter = authWriter;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        CustomAuthUser customOAuth2User = (CustomAuthUser) authentication.getPrincipal();
        Long memberId = customOAuth2User.getMemberId();

        String accessToken = jwtService.createAccessToken(memberId);
        String refreshToken = jwtService.createRefreshToken(memberId);
        response.addCookie(cookieService.createAccessTokenCookie(accessToken));
        response.addCookie(cookieService.createRefreshTokenCookie(refreshToken));
        authWriter.saveRefreshToken(memberId, refreshToken);

        String redirectUrl;
        log.info("[cookie : host] " + request.getHeader("host"));
        String host = request.getHeader("host");
        if (host.contains("front.dongsanwalk.site")) {
            redirectUrl = devRedirectUrl;
        } else {
            redirectUrl = prodRedirectUrl;
        }
        response.sendRedirect(redirectUrl);
    }
}
