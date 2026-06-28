package com.forge.devopstoolbox.api.websocket;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
class WebSocketConfig implements WebSocketConfigurer {

    private final PlatformWebSocketHandler platformWebSocketHandler;

    WebSocketConfig(PlatformWebSocketHandler platformWebSocketHandler) {
        this.platformWebSocketHandler = platformWebSocketHandler;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(platformWebSocketHandler, "/ws")
                .setAllowedOrigins("*");
    }
}
