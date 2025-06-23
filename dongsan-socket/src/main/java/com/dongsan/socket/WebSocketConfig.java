package com.dongsan.socket;

import com.dongsan.socket.authenticate.SocketCookieService;
import com.dongsan.socket.authenticate.SocketJwtService;
import com.dongsan.socket.authenticate.WebSocketHandshakeHandler;
import com.dongsan.socket.authenticate.WebSocketHandshakeInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private final SocketJwtService socketJwtService;
    private final SocketCookieService socketCookieService;

    public WebSocketConfig(SocketJwtService socketJwtService, SocketCookieService socketCookieService) {
        this.socketJwtService = socketJwtService;
        this.socketCookieService = socketCookieService;
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
                .setAllowedOrigins("*")
                .setAllowedOriginPatterns("*")  // 이거 위의 두개랑 차이 뭐지
                .addInterceptors(new WebSocketHandshakeInterceptor(socketCookieService, socketJwtService))
                .setHandshakeHandler(new WebSocketHandshakeHandler())
                .withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic");
        registry.setApplicationDestinationPrefixes("/app");
    }

}
