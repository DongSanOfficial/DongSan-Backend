package com.dongsan.api.domains.dev;

import com.dongsan.api.domains.auth.AuthService;
import com.dongsan.api.support.response.ApiResponse;
import com.dongsan.core.domains.auth.GetTokenRemaining;
import com.dongsan.core.domains.auth.TokenReader;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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
    public ApiResponse<Void> generateToken(
            @RequestBody GenerateTokenRequest dto,
            HttpServletResponse httpServletResponse
    ){
        devService.generateToken(dto.memberId(), httpServletResponse);
        return ApiResponse.success(null);
    }

    @Operation(summary = "accessToken으로 member 정보 확인하기")
    @GetMapping("/members")
    public ApiResponse<GetMemberInfoResponse> getMemberInfo(
            HttpServletRequest request
    ){
        GetMemberInfoResponse response = devService.getMemberInfo(request);
        return ApiResponse.success(response);
    }

    @Operation(summary = "memberId로 서버에 저장된 refreshToken 확인하기")
    @GetMapping("/token/refresh")
    public ApiResponse<Map<String, String>> getRefreshToken(
            @RequestParam Long memberId
    ){
        String refreshToken = tokenReader.getRefreshToken(memberId);
        Map<String, String> response = new HashMap<>();
        response.put("refreshToken", refreshToken);
        return ApiResponse.success(response);
    }

    @Operation(summary = "access token, refresh token의 만료기간 확인")
    @PostMapping("/token/expired")
    public ApiResponse<GetTokenRemaining> checkTokenExpire(
            HttpServletRequest request
    ){
        GetTokenRemaining response = authService.checkTokenExpire(request);
        return ApiResponse.success(response);
    }

    @Operation(summary = "이미지 s3 저장 확인하기")
    @PostMapping(value = "/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<String> uploadImage(
            @RequestPart("file") MultipartFile image
    )throws IOException {
        String url = devService.uploadImage(image);
        return ApiResponse.success(url);
    }

}
