package com.example.CocO.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // /queue는 클라이언트로 브로드캐스트할 메시지에 대한 경로
        config.enableSimpleBroker("/queue");

        // /app은 클라이언트가 서버로 보낼 메시지에 대한 경로
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // 클라이언트가 연결할 수 있는 웹소켓 엔드포인트를 설정
        registry.addEndpoint("/ws")  // /ws 경로로 연결을 설정
                .setAllowedOriginPatterns("*")  // 모든 출처에서 요청을 허용
                .withSockJS();  // SockJS를 사용하여 연결 안정성을 높임
    }
}
