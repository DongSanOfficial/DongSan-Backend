package com.dongsan.api.support.logging.dto;

import org.springframework.http.HttpStatus;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.util.Collections;
import java.util.stream.Collectors;

public record HttpLogMessage(
        String httpMethod,
        String requestURI,
        HttpStatus httpStatus,
        String clientIp,
        double elapsedTime,
        String headers,
        String requestParam,
        String requestBody,
        String responseBody
) {
    public static HttpLogMessage createInstance(ContentCachingRequestWrapper requestWrapper, ContentCachingResponseWrapper responseWrapper, double elapsedTime){
        String headers = Collections.list(requestWrapper.getHeaderNames())
                .stream()
                .map(header -> header + " : "+requestWrapper.getHeader(header))
                .collect(Collectors.joining(", "));

        String requestBody = new String(requestWrapper.getContentAsByteArray());
        String responseBody = new String(responseWrapper.getContentAsByteArray());

        return new HttpLogMessage(
                requestWrapper.getMethod(),
                requestWrapper.getRequestURI(),
                HttpStatus.valueOf(responseWrapper.getStatus()),
                requestWrapper.getRemoteAddr(),
                elapsedTime,
                headers,
                requestWrapper.getQueryString(),
                requestBody,
                responseBody
        );
    }

    public String toPrettierLog(){
        return String.format(
                """
                     
                |[REQUEST] %s %s %s (%.3f)
                |>> CLIENT_IP: %s
                |>> HEADERS: %s
                |>> REQUEST_PARAM: %s
                |>> REQUEST_BODY: %s
                |>> RESPONSE_BODY: %s
                """,
                this.httpMethod,
                this.requestURI,
                this.httpStatus,
                this.elapsedTime,
                this.clientIp,
                this.headers,
                this.requestParam,
                this.requestBody,
                this.responseBody
        );
    }
}
