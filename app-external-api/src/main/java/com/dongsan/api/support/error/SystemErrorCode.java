package com.dongsan.api.support.error;

import org.springframework.http.HttpStatus;

public enum SystemErrorCode {
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "SYSTEM-01", "서버 내부 오류가 발생했습니다. 다시 시도해주세요."),
    SERVICE_UNAVAILABLE(HttpStatus.SERVICE_UNAVAILABLE, "SYS-02", "현재 서비스 이용이 불가능합니다. 나중에 다시 시도해주세요."),
    GATEWAY_TIMEOUT(HttpStatus.GATEWAY_TIMEOUT, "SYS-03", "요청 시간이 초과되었습니다. 다시 시도해주세요."),
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    SystemErrorCode(HttpStatus httpStatus, String code, String message) {
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
