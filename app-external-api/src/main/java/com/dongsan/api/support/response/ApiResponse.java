package com.dongsan.api.support.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;

public class ApiResponse<T> {
    private final Boolean isSuccess;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final String code;
    private final String message;
    private final T data;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private final List<ValidationError> errors; // 실패 시 에러 목록

    private final static String SUCCESS_MESSAGE = "요청이 성공적으로 처리되었습니다.";

    public ApiResponse(Boolean isSuccess, String code, String message, T data, List<ValidationError> errors) {
        this.isSuccess = isSuccess;
        this.code = code;
        this.message = message;
        this.data = data;
        this.errors = errors;
    }

    public static <T> ApiResponse<T> success(){
        return new ApiResponse<>(true, null, SUCCESS_MESSAGE, null, null);
    }

    public static <T> ApiResponse<T> success(T data){
        return new ApiResponse<>(true, null, SUCCESS_MESSAGE, data, null);
    }

    public static <T> ApiResponse<T> error(String code, String message){
        return new ApiResponse<>(false, code, message, null, null);
    }

    public static <T> ApiResponse<T> error(String code, String message, List<ValidationError> errors){
        return new ApiResponse<>(false, code, message, null, errors);
    }

    public Boolean getSuccess() {
        return isSuccess;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }

    public List<ValidationError> getErrors() {
        return errors;
    }
}
