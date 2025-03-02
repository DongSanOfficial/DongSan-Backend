package com.dongsan.api.support.controller;

import com.dongsan.api.support.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "개발용 API")
public class HealthController {

    @Operation(summary = "애플리케이션 헬스체크")
    @GetMapping("/health")
    public ApiResponse<String> health(){
        return ApiResponse.success("Server is Healthy!");
    }

    @Operation(summary = "홈 헬스체크")
    @GetMapping("/")
    public ApiResponse<String> home(){
        return ApiResponse.success("It's Home!");
    }
}
