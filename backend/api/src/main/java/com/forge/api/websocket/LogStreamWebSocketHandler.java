package com.forge.api.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.forge.core.event.Event;
import com.forge.core.event.EventBus;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * WebSocket handler for live log streaming.
 * Path: /ws/logs/stream?source={containerId}
 *
 * On connect: starts `docker logs -f {containerId}` and streams each line as JSON.
 * Server → Client: { "type": "log", "data": "line text" }
 *                  { "type": "done" }  (when stream ends)
 */
@Component
public class LogStreamWebSocketHandler extends TextWebSocketHandler {

    private final EventBus eventBus;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final ConcurrentHashMap<String, Process> processes = new ConcurrentHashMap<>();

    public LogStreamWebSocketHandler(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession wsSession) {
        String source = extractParam(wsSession, "source");
        if (source == null || source.isBlank()) {
            closeWith(wsSession, CloseStatus.BAD_DATA.withReason("'source' query param required"));
            return;
        }

        eventBus.publish(new Event("LogsStreamingStarted", null, "Log streaming started for: " + source));

        Thread.ofVirtual().start(() -> {
            try {
                Process process = new ProcessBuilder("docker", "logs", "-f", "--tail", "50", source)
                        .redirectErrorStream(true)
                        .start();

                processes.put(wsSession.getId(), process);

                try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        if (!wsSession.isOpen()) break;
                        String msg = objectMapper.writeValueAsString(Map.of("type", "log", "data", line));
                        wsSession.sendMessage(new TextMessage(msg));
                    }
                }

                processes.remove(wsSession.getId());
                if (wsSession.isOpen()) {
                    wsSession.sendMessage(new TextMessage("{\"type\":\"done\"}"));
                }
                eventBus.publish(new Event("LogsStreamingStopped", null, "Log streaming ended for: " + source));

            } catch (Exception e) {
                sendText(wsSession, "{\"type\":\"error\",\"message\":\"" + e.getMessage() + "\"}");
            }
        });
    }

    @Override
    public void afterConnectionClosed(WebSocketSession wsSession, CloseStatus status) {
        Process p = processes.remove(wsSession.getId());
        if (p != null && p.isAlive()) p.destroyForcibly();
    }

    private String extractParam(WebSocketSession session, String name) {
        String query = session.getUri() != null ? session.getUri().getQuery() : null;
        if (query == null) return null;
        for (String pair : query.split("&")) {
            String[] kv = pair.split("=", 2);
            if (kv.length == 2 && kv[0].equals(name)) return kv[1];
        }
        return null;
    }

    private void sendText(WebSocketSession session, String text) {
        try { if (session.isOpen()) session.sendMessage(new TextMessage(text)); } catch (Exception ignored) {}
    }

    private void closeWith(WebSocketSession session, CloseStatus status) {
        try { session.close(status); } catch (Exception ignored) {}
    }
}
