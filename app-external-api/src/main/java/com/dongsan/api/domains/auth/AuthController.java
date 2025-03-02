package com.dongsan.api.domains.auth;

import com.dongsan.api.domains.auth.security.oauth2.CustomOAuth2User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@Tag(name = "인증")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @Operation(summary = "Access token 재발급 (Refresh Token을 받으면 Access token & Refresh Token 재발급, Refresh Token 만료 시 다시 로그인 진행해야 함)")
    @PostMapping("/refresh")
    public ResponseEntity<Void> renewToken(
            HttpServletRequest request,
            HttpServletResponse response
    ){
        authService.renewToken(request, response);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "로그 아웃")
    @DeleteMapping("/logout")
    public ResponseEntity<Void> logout(
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User,
            HttpServletResponse response
    ){
        authService.logout(customOAuth2User.getMemberId(), response);
        return ResponseEntity.ok().build();
    }
}
