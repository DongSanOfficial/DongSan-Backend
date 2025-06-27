package com.dongsan.socket.authenticate;

import com.dongsan.domain.domains.member.Member;
import com.dongsan.domain.support.error.CoreException;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

public class WebSocketHandshakeInterceptor implements HandshakeInterceptor {
    private static final Logger log = LoggerFactory.getLogger(WebSocketHandshakeInterceptor.class);
    private final SocketCookieService socketCookieService;
    private final SocketJwtService socketJwtService;

    public WebSocketHandshakeInterceptor(SocketCookieService socketCookieService, SocketJwtService socketJwtService) {
        this.socketCookieService = socketCookieService;
        this.socketJwtService = socketJwtService;
    }

    // WebSocket 연결 시점의 핸드쉐이크 과정에서 호출되는데, 이 단계에서는 클라이언트와 아직 연결이 완료된 상태가 아니고, 메시지를 주고받을 수 있는 상태가 아님
    // 예외를 잡아서 로그를 남기고, false를 반환해 연결을 차단하는 방식으로 처리
    @Override
    public boolean beforeHandshake(ServerHttpRequest request,
                                   ServerHttpResponse response,
                                   WebSocketHandler wsHandler,
                                   Map<String, Object> attributes) throws Exception {
        log.info("[websocket] beforeHandshake 진입");
        if (!(request instanceof ServletServerHttpRequest servletRequest)) return false;

        HttpServletRequest httpServletRequest = servletRequest.getServletRequest();
        try {
            String accessToken = socketCookieService.getAccessTokenFromCookie(httpServletRequest);
            log.info("[websocket] accessToken : {}", accessToken);

            if (!socketJwtService.isAccessTokenExpired(accessToken)) {
                Member member = socketJwtService.getMemberFromAccessToken(accessToken);
                log.info("[websocket] memberId : {}", member.getId());
                SocketUserPrincipal user = new SocketUserPrincipal(member);
                attributes.put("user", user);  // 세션에 넣기
                return true;
            }
        } catch (CoreException e) {
            log.warn("websocket 인증 실패 : {}", e.getMessage());
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return false;
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request,
                               ServerHttpResponse response,
                               WebSocketHandler wsHandler,
                               Exception exception) {
        // nothing
    }
}
