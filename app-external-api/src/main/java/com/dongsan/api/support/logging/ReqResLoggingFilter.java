package com.dongsan.api.support.logging;

import com.dongsan.api.support.logging.dto.HttpLogMessage;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ReqResLoggingFilter extends OncePerRequestFilter {
    private static final Logger log = LoggerFactory.getLogger(ReqResLoggingFilter.class);
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        ContentCachingRequestWrapper cachingRequestWrapper = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper cachingResponseWrapper = new ContentCachingResponseWrapper(response);

        long startTime = System.currentTimeMillis();
        filterChain.doFilter(cachingRequestWrapper, cachingResponseWrapper);
        long endTime = System.currentTimeMillis();

        try{
            HttpLogMessage httpLogMessage = HttpLogMessage.createInstance(cachingRequestWrapper, cachingResponseWrapper, (endTime-startTime)/1000.0);
            log.info(httpLogMessage.toPrettierLog());
        } catch (Exception e){
            log.warn("[Logging] " + this.getClass().getSimpleName() + "Logging 실패");
        }

        // 캐시된 응답 본문을 클라이언트로 전송
        cachingResponseWrapper.copyBodyToResponse();
    }

    /**
     * Swagger 에 대한 HTTP 로깅을 비활성하기 위한 설정
     * <p>
     * doFilterInternal 메서드가 호출되기 전에 souldNotFilter 메서드를 호출해 필터를 적용할지 결정한다. shouldNotFilter 메서드가 false 를 반환하면
     * doFilterInternal 는 호출되지 않고 해당 요청에 대해 필터가 적용되지 않는다.
     */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String requestURI = request.getRequestURI();
        return requestURI.startsWith("/swagger-ui") ||
                requestURI.startsWith("/v3/api-docs") ||
                requestURI.equals("/swagger-ui.html");
    }
}
