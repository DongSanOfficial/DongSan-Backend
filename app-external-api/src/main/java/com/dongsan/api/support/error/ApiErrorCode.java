package com.dongsan.api.support.error;

import org.springframework.http.HttpStatus;

public enum ApiErrorCode {
    ILLEGAL_ARGUMENT_ERROR(HttpStatus.BAD_REQUEST, "CLIENT-01", "잘못된 요청입니다. 요청 내용을 다시 확인해주세요."),
    NOT_FOUND(HttpStatus.NOT_FOUND, "CLIENT-02", "요청한 리소스를 찾을 수 없습니다."),
    INVALID_ARGUMENT_ERROR(HttpStatus.BAD_REQUEST, "CLIENT-03", "올바르지 않은 요청입니다. 요청 내용을 다시 확인해주세요."),
    INVALID_FORMAT_ERROR(HttpStatus.BAD_REQUEST,"CLIENT-04", "올바르지 않은 포맷입니다."),
    INVALID_TYPE_ERROR(HttpStatus.BAD_REQUEST, "CLIENT-05", "올바르지 않은 타입입니다."),
    INVALID_HTTP_METHOD(HttpStatus.METHOD_NOT_ALLOWED, "CLIENT-06", "잘못된 Http Method 요청입니다."),

    // 인증 및 권한 관련 에러
    AUTHENTICATION_FAILED(HttpStatus.UNAUTHORIZED, "AUTH-01", "사용자 인증에 실패했습니다."),
    ACCESS_DENIED(HttpStatus.FORBIDDEN, "AUTH-02", "접근이 거부되었습니다. 이 리소스에 대한 권한이 없습니다."),
    INSUFFICIENT_PERMISSIONS(HttpStatus.FORBIDDEN, "AUTH-03", "작업을 수행할 권한이 부족합니다."),
    LOGIN_FAILED(HttpStatus.UNAUTHORIZED, "AUTH-04", "로그인에 실패했습니다."),

    // 토큰 관련 에러
    ACCESS_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "AUTH-05", "엑세스 토큰의 유효기간이 만료되었습니다."),
    REFRESH_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "AUTH-06", "리프레시 토큰의 유효기간이 만료되었습니다."),
    INVALID_TOKEN_FORMAT(HttpStatus.BAD_REQUEST, "AUTH-07", "잘못된 토큰 형식입니다."),
    INVALID_TOKEN_SIGNATURE(HttpStatus.BAD_REQUEST, "AUTH-08", "토큰의 서명이 일치하지 않습니다."),
    UNSUPPORTED_TOKEN(HttpStatus.BAD_REQUEST, "AUTH-09", "토큰의 특정 헤더나 클레임이 지원되지 않습니다."),
    ACCESS_TOKEN_NOT_FOUND(HttpStatus.UNAUTHORIZED, "AUTH-10", "쿠키에 엑세스 토큰이 없습니다."),
    REFRESH_TOKEN_NOT_FOUND(HttpStatus.UNAUTHORIZED, "AUTH-11", "쿠키에 리프레시 토큰이 없습니다."),
    JWT_VALIDATION_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "AUTH-12", "JWT 토큰 만료 검사중 알 수 없는 서버 오류 발생")
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    ApiErrorCode(HttpStatus httpStatus, String code, String message) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.message = message;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
