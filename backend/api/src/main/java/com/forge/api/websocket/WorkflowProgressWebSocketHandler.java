package com.forge.api.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.forge.core.event.Event;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Streams workflow progress events to the UI.
 * Connects to the core EventBus indirectly by listening to Spring ApplicationEvents published by InMemoryEventBus.
 */
@Component
public class WorkflowProgressWebSocketHandler extends TextWebSocketHandler {

    private final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        sessions.put(session.getId(), session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        sessions.remove(session.getId());
    }

    @EventListener
    public void handleEvent(Event event) {
        // Only broadcast workflow-related events
        if (event.getEventType().startsWith("Workflow")) {
            broadcast(event);
        }
    }

    private void broadcast(Event event) {
        if (sessions.isEmpty()) return;

        try {
            String json = objectMapper.writeValueAsString(event);
            TextMessage message = new TextMessage(json);
            
            for (WebSocketSession session : sessions.values()) {
                if (session.isOpen()) {
                    try {
                        session.sendMessage(message);
                    } catch (IOException e) {
                        // Ignore individual send failures
                    }
                }
            }
        } catch (Exception e) {
            // Ignore serialization errors
        }
    }
}
