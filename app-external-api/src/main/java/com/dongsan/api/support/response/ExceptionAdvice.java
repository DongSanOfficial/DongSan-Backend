package com.dongsan.api.support.response;

import com.dongsan.api.support.error.ApiErrorCode;
import com.dongsan.api.support.error.ApiException;
import com.dongsan.api.support.error.SystemErrorCode;
import com.dongsan.core.support.error.CoreErrorCode;
import com.dongsan.core.support.error.CoreErrorStatus;
import com.dongsan.core.support.error.CoreException;
import jakarta.validation.ConstraintViolationException;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.NestedExceptionUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice(annotations = {RestController.class})
public class ExceptionAdvice extends ResponseEntityExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(ExceptionAdvice.class);

    @ExceptionHandler(value = CoreException.class)
    public ResponseEntity<ApiResponse> handleCoreException(CoreException e) {
        CoreErrorCode errorCode = e.getErrorCode();
        CoreErrorStatus status = e.getErrorCode().getHttpStatus();
        HttpStatus httpStatus = switch (status){
            case BAD_REQUEST -> HttpStatus.BAD_REQUEST;
            case FORBIDDEN -> HttpStatus.FORBIDDEN;
            case NOT_FOUND -> HttpStatus.NOT_FOUND;
            case CONFLICT -> HttpStatus.CONFLICT;
        };
        log.error("[CoreException] cause: {}, message: {}", NestedExceptionUtils.getMostSpecificCause(e), e.getMessage());
        return ResponseEntity.status(httpStatus).body(ApiResponse.error(errorCode.getCode(), errorCode.getMessage()));
    }

    @ExceptionHandler(value = ApiException.class)
    public ResponseEntity<ApiResponse> handleApiException(ApiException e){
        ApiErrorCode errorCode = e.getErrorCode();
        log.error("[ApiException] cause: {}, message: {}", NestedExceptionUtils.getMostSpecificCause(e), e.getMessage());
        return ResponseEntity.status(errorCode.getHttpStatus()).body(ApiResponse.error(errorCode.getCode(),
                errorCode.getMessage()));
    }

    // exception 을 상속받는 모든 예외 처리
    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ApiResponse> handleException(Exception e) {
        log.error("[Exception] cause: {} , message: {}", NestedExceptionUtils.getMostSpecificCause(e), e.getMessage());
        SystemErrorCode errorCode = SystemErrorCode.INTERNAL_SERVER_ERROR;
        return ResponseEntity.status(errorCode.getHttpStatus()).body(ApiResponse.error(errorCode.getCode(), errorCode.getMessage()));
    }


    // 메소드가 잘못되었거나 부적절한 인수를 전달했을 때 -> 필수 파라미터가 없을 때
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse> handleIllegalArgumentException(IllegalArgumentException e){
        log.error("[IlleagalArgumentException] cause: {} , message: {}", NestedExceptionUtils.getMostSpecificCause(e),e.getMessage());
        ApiErrorCode errorCode = ApiErrorCode.ILLEGAL_ARGUMENT_ERROR;
        String errorMessage = String.format("%s %s", errorCode.getMessage(), NestedExceptionUtils.getMostSpecificCause(e).getMessage());
        return ResponseEntity.status(errorCode.getHttpStatus()).body(ApiResponse.error(errorCode.getCode(), errorMessage));
    }

    // 컨트롤러 인자(@PathVariable, @RequestParam) 예외 처리
    @ExceptionHandler
    public ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException e) {
        log.error("[ConstraintViolationException] cause: {} , message: {}", NestedExceptionUtils.getMostSpecificCause(e),e.getMessage());
        ApiErrorCode errorCode = ApiErrorCode.INVALID_ARGUMENT_ERROR;
        List<ValidationError> errors = e.getConstraintViolations()
                .stream()
                .map(violation -> new ValidationError(violation.getPropertyPath()
                        .toString(), violation.getMessage()))
                .toList();
        return ResponseEntity.status(errorCode.getHttpStatus()).body(ApiResponse.error(errorCode.getCode(), errorCode.getMessage(), errors));
    }

    // DTO 유효성(@RequestBody) 관련 예외 처리
    @Override
    public ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException e, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        log.error("[MethodArgumentNotValidException] cause: {} , message: {}", NestedExceptionUtils.getMostSpecificCause(e), e.getMessage());
        ApiErrorCode errorCode = ApiErrorCode.INVALID_ARGUMENT_ERROR;
        List<FieldError> fieldErrors = e.getBindingResult()
                .getFieldErrors();
        List<ValidationError> errors = fieldErrors.stream()
                .map(fieldError -> new ValidationError(fieldError.getField(), fieldError.getDefaultMessage()))
                .toList();
        return ResponseEntity.status(errorCode.getHttpStatus()).body(ApiResponse.error(errorCode.getCode(), errorCode.getMessage(), errors));
    }

}
