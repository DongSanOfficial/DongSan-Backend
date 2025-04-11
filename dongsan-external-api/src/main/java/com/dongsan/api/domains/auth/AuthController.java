package com.dongsan.api.domains.auth;

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

    @Operation(summary = "Access token 재발급 (Refresh Token을 받으면 Access token & Refresh Token 재발급, Refresh Token 만료 시 다시"
            + " 로그인 진행해야 함)")
    @PostMapping("/refresh")
    public ResponseEntity<Void> renewToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        authService.renewToken(request, response);
        return ResponseEntity.ok().build();
    }

    /**
     * 우선은 빠른 재심사를 위해 로그아웃과 동일하게 토큰만 삭제하는 걸로 되어 있음.
     * (개선 방식)
     * 필드 추가해서 isDeactivated = true, deactivatedAt 설정
     * 14일 지나면 데이터 아예 삭제 (spring batch 사용)
     * 14일 내로 돌아오면 탈퇴하지 않은 걸로 다시 활동
     * 삭제되거나 deactivate 된 사용자는 (알수없음) 으로 닉네임이 뜨도록 설정 필요 (도메인 객체에서 따로 매핑 해야 할듯)
     */
    @Operation(summary = "로그 아웃")
    @DeleteMapping("/logout")
    public ResponseEntity<Void> logout(
            @AuthenticationPrincipal CustomAuthUser customOAuth2User,
            HttpServletResponse response
    ) {
        authService.logout(customOAuth2User.getMemberId(), response);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "탈퇴하기")
    @DeleteMapping("/deactivate")
    public ResponseEntity<Void> deactivate(
            @AuthenticationPrincipal CustomAuthUser customAuthUser,
            HttpServletResponse response
    ) {
        authService.logout(customAuthUser.getMemberId(), response);
        return ResponseEntity.ok().build();
    }
}
