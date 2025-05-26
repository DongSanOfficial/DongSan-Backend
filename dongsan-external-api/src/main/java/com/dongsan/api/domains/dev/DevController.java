package com.dongsan.api.domains.dev;

import com.dongsan.api.domains.auth.AuthService;
import com.dongsan.domain.domains.refresh.GetTokenRemaining;
import com.dongsan.domain.domains.refresh.TokenReader;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/dev")
@Tag(name = "개발용 API", description = "Develop API")
@Validated
public class DevController {
    private final DevService devService;
    private final AuthService authService;
    private final TokenReader tokenReader;

    public DevController(DevService devService, AuthService authService,
                         TokenReader tokenReader) {
        this.devService = devService;
        this.authService = authService;
        this.tokenReader = tokenReader;
    }

    @Operation(summary = "개발용 토큰 발급")
    @PostMapping("/token")
    public ResponseEntity<Void> generateToken(
            @RequestBody GenerateTokenRequest dto,
            HttpServletResponse httpServletResponse
    ) {
        devService.generateToken(dto.memberId(), httpServletResponse);
        return ResponseEntity.ok()
                .build();
    }

    @Operation(summary = "accessToken으로 member 정보 확인하기")
    @GetMapping("/members")
    public ResponseEntity<MemberInfoResponse> getMemberInfo(
            HttpServletRequest request
    ) {
        MemberInfoResponse response = devService.getMemberInfo(request);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "memberId로 서버에 저장된 refreshToken 확인하기")
    @GetMapping("/token/refresh")
    public ResponseEntity<Map<String, String>> getRefreshToken(
            @RequestParam Long memberId
    ) {
        String refreshToken = tokenReader.getRefreshToken(memberId);
        Map<String, String> response = new HashMap<>();
        response.put("refreshToken", refreshToken);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "access token, refresh token의 만료기간 확인")
    @PostMapping("/token/expired")
    public ResponseEntity<GetTokenRemaining> checkTokenExpire(
            HttpServletRequest request
    ) {
        GetTokenRemaining response = authService.checkTokenExpire(request);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "이미지 s3 저장 확인하기")
    @PostMapping(value = "/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces =
            MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, String>> uploadImage(
            @RequestPart("file") MultipartFile image
    ) throws IOException {
        String url = devService.uploadImage(image);
        Map<String, String> response = Map.of("url", url);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "에러 발생 테스트")
    @GetMapping("/exception")
    public void exception() throws Exception {
        throw new Exception("에러 발생");
    }
}
