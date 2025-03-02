package com.dongsan.api.domains.auth.security.handler;

import com.dongsan.api.support.error.ApiErrorCode;
import com.dongsan.api.support.response.ApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private static final Logger log = LoggerFactory.getLogger(CustomAuthenticationEntryPoint.class);
    private final ObjectMapper objectMapper;

    public CustomAuthenticationEntryPoint(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {
        Object errorObj = request.getAttribute("error");
        ApiErrorCode errorCode = errorObj instanceof ApiErrorCode ? (ApiErrorCode) errorObj : ApiErrorCode.AUTHENTICATION_FAILED;
        ResponseEntity<ApiResponse> errorResponse = ResponseEntity.status(errorCode.getHttpStatus()).body(ApiResponse.error(errorCode.getCode(), errorCode.getMessage()));
        response.setContentType("application/json");
        // charset=UTF-8 을 붙이지 않으면 message 가 ??? 로 깨져서 표시된다.
        response.setCharacterEncoding("UTF-8");
        response.setStatus(errorResponse.getStatusCode()
                .value());
        response.getWriter()
                .write(new ObjectMapper().writeValueAsString(errorResponse.getBody()));
        response.getWriter()
                .flush();
    }
}
