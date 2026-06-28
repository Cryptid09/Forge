package com.forge.api.websocket;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    private final EventWebSocketHandler eventWebSocketHandler;
    private final WorkflowProgressWebSocketHandler workflowProgressWebSocketHandler;
    private final TerminalWebSocketHandler terminalWebSocketHandler;
    private final LogStreamWebSocketHandler logStreamWebSocketHandler;

    public WebSocketConfig(EventWebSocketHandler eventWebSocketHandler, 
                           WorkflowProgressWebSocketHandler workflowProgressWebSocketHandler,
                           TerminalWebSocketHandler terminalWebSocketHandler,
                           LogStreamWebSocketHandler logStreamWebSocketHandler) {
        this.eventWebSocketHandler = eventWebSocketHandler;
        this.workflowProgressWebSocketHandler = workflowProgressWebSocketHandler;
        this.terminalWebSocketHandler = terminalWebSocketHandler;
        this.logStreamWebSocketHandler = logStreamWebSocketHandler;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(eventWebSocketHandler, "/ws/events")
                .setAllowedOrigins("*");
        registry.addHandler(workflowProgressWebSocketHandler, "/ws/workflow/progress")
                .setAllowedOrigins("*");
        registry.addHandler(terminalWebSocketHandler, "/ws/terminal")
                .setAllowedOrigins("*");
        registry.addHandler(logStreamWebSocketHandler, "/ws/logs/stream")
                .setAllowedOrigins("*");
    }
}
