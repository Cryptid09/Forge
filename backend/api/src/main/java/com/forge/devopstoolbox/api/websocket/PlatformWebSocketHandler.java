package com.forge.devopstoolbox.api.websocket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.forge.devopstoolbox.api.dto.EventResponse;
import com.forge.devopstoolbox.api.mapper.PlatformMapper;
import com.forge.devopstoolbox.core.model.PlatformEvent;
import com.forge.devopstoolbox.core.port.EventBus;
import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
public class PlatformWebSocketHandler extends TextWebSocketHandler {

    private static final Logger log = LoggerFactory.getLogger(PlatformWebSocketHandler.class);

    private final EventBus eventBus;
    private final ObjectMapper objectMapper;
    private final Set<WebSocketSession> sessions = ConcurrentHashMap.newKeySet();

    public PlatformWebSocketHandler(EventBus eventBus, ObjectMapper objectMapper) {
        this.eventBus = eventBus;
        this.objectMapper = objectMapper;
    }

    @PostConstruct
    void subscribeToEvents() {
        eventBus.subscribe(this::broadcastEvent);
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        sessions.add(session);
        log.info("WebSocket connection established: sessionId={}", session.getId());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        sessions.remove(session);
        log.info("WebSocket connection closed: sessionId={} status={}", session.getId(), status);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
        // Phase 1: server-push only; client messages are ignored.
    }

    private void broadcastEvent(PlatformEvent event) {
        EventResponse response = PlatformMapper.toEventResponse(event);
        WebSocketMessage payload = new WebSocketMessage("event", response);
        broadcast(payload);
    }

    private void broadcast(WebSocketMessage message) {
        String json;
        try {
            json = objectMapper.writeValueAsString(message);
        } catch (JsonProcessingException exception) {
            log.error("Failed to serialize WebSocket message", exception);
            return;
        }

        TextMessage textMessage = new TextMessage(json);
        for (WebSocketSession session : sessions) {
            if (session.isOpen()) {
                try {
                    session.sendMessage(textMessage);
                } catch (IOException exception) {
                    log.warn("Failed to send WebSocket message: sessionId={}", session.getId(), exception);
                }
            }
        }
    }

    public record WebSocketMessage(String type, Object data) {
    }
}
