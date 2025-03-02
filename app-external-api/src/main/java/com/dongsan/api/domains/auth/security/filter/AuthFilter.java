package com.dongsan.api.domains.auth.security.filter;

import com.dongsan.api.domains.auth.CookieService;
import com.dongsan.api.domains.auth.JwtService;
import com.dongsan.api.domains.auth.security.oauth2.CustomOAuth2User;
import com.dongsan.api.support.error.ApiErrorCode;
import com.dongsan.api.support.error.ApiException;
import com.dongsan.core.domains.auth.TokenReader;
import com.dongsan.core.domains.auth.TokenWriter;
import com.dongsan.core.domains.member.Member;
import com.dongsan.core.support.error.CoreException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

public class AuthFilter extends OncePerRequestFilter {
    private static final Logger log = LoggerFactory.getLogger(AuthFilter.class);
    private final JwtService jwtService;
    private final CookieService cookieService;
    private final TokenReader tokenReader;
    private final TokenWriter tokenWriter;

    public AuthFilter(JwtService jwtService, CookieService cookieService,
                      TokenReader tokenReader, TokenWriter tokenWriter) {
        this.jwtService = jwtService;
        this.cookieService = cookieService;
        this.tokenReader = tokenReader;
        this.tokenWriter = tokenWriter;
    }

    /**
     * 필터 안 타는 것들 여기에 작성
     */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        Set<String> excludeUrl = new HashSet<>(Set.of(
        ));

        String url = request.getRequestURI();
        return excludeUrl.stream().anyMatch(url::startsWith);
    }

    /**
     * access 만료 (쿠키에 없음) <br>
     *
     *   1. refresh 만료 0
     *     -> 로그인 하라는 에러 반환 <br>
     *
     *   2. refresh 만료 X
     *     -> 토큰 둘다 재발급 후 쿠키에 다시 세팅 후 로직 수행
     * @param request
     * @param response
     * @param filterChain
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try{
            String accessToken = cookieService.getAccessTokenFromCookie(request);
            if(jwtService.isAccessTokenExpired(accessToken)){
                String refreshToken = cookieService.getRefreshTokenFromCookie(request);
                if (!jwtService.isRefreshTokenExpired(refreshToken)) {
                    Member member = jwtService.getMemberFromRefreshToken(refreshToken);
                    if (tokenReader.isRefreshTokenNotReplaced(member.id(), refreshToken)) {
                        String newAccessToken = jwtService.createAccessToken(member.id());
                        String newRefreshToken = jwtService.createRefreshToken(member.id());
                        response.addCookie(cookieService.createAccessTokenCookie(newAccessToken));
                        response.addCookie(cookieService.createRefreshTokenCookie(newRefreshToken));
                        tokenWriter.saveRefreshToken(member.id(), newRefreshToken);
                        authenticateUser(member);
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
            else{
                authenticateUser(jwtService.getMemberFromAccessToken(accessToken));
            }
        } catch (CoreException ex){
            log.error("[AUTH] CustomException 발생 : {}", ex.getMessage());
            request.setAttribute("error", ex.getErrorCode());
        } catch (Exception e){
            log.error("[AUTH] auth filter 에서 문제 발생 : {}", e.getMessage());
            request.setAttribute("error", null);
        }

        filterChain.doFilter(request, response);
    }

    private void authenticateUser(Member member) {
        log.info("[AUTH] auth filter 사용자 정보, email : {}", member.email());
        CustomOAuth2User customOAuth2User = new CustomOAuth2User(member);
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                customOAuth2User,
                null,
                customOAuth2User.getAuthorities()
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
