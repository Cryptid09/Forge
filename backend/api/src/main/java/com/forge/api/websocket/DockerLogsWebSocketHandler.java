package com.forge.api.websocket;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class DockerLogsWebSocketHandler extends TextWebSocketHandler {

    // Note: in a real scenario we'd inject DockerClient via DockerFacade or directly
    // This is a stub implementation for the architecture proof
    
    private final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        sessions.put(session.getId(), session);
        try {
            session.sendMessage(new TextMessage("Connected to log stream"));
        } catch (IOException e) {
            // Ignore
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        sessions.remove(session.getId());
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String containerId = message.getPayload();
        session.sendMessage(new TextMessage("Streaming logs for " + containerId + "..."));
        // Mock log output
        new Thread(() -> {
            try {
                for(int i=0; i<5; i++) {
                    Thread.sleep(1000);
                    if(session.isOpen()) {
                        session.sendMessage(new TextMessage("Log entry " + i + " from " + containerId));
                    }
                }
            } catch (Exception e) {}
        }).start();
    }
}
