package com.forge.api.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.forge.observability.terminal.TerminalSessionRegistry;
import com.forge.observability.terminal.TerminalSession;
import com.forge.observability.terminal.TerminalSessionStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * WebSocket handler for interactive terminal output.
 * Path: /ws/terminal?sessionId={sessionId}
 *
 * Protocol:
 *   Client → Server: JSON string { "command": "ls -la" }
 *   Server → Client: raw stdout/stderr text, line by line
 */
@Component
public class TerminalWebSocketHandler extends TextWebSocketHandler {

    private final TerminalSessionRegistry sessionRegistry;
    private final ObjectMapper objectMapper = new ObjectMapper();

    // Maps WebSocket session ID → active subprocess
    private final ConcurrentHashMap<String, Process> activeProcesses = new ConcurrentHashMap<>();

    public TerminalWebSocketHandler(TerminalSessionRegistry sessionRegistry) {
        this.sessionRegistry = sessionRegistry;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession wsSession) throws Exception {
        String sessionId = extractParam(wsSession, "sessionId");
        if (sessionId == null) {
            wsSession.close(CloseStatus.BAD_DATA.withReason("sessionId query param required"));
            return;
        }
        sessionRegistry.findById(sessionId).ifPresentOrElse(
                s -> sendText(wsSession, "{\"type\":\"connected\",\"sessionId\":\"" + sessionId + "\",\"cwd\":\"" + s.getWorkingDirectory() + "\"}"),
                () -> {
                    try { wsSession.close(CloseStatus.BAD_DATA.withReason("Session not found")); } catch (Exception ignored) {}
                });
    }

    @Override
    protected void handleTextMessage(WebSocketSession wsSession, TextMessage message) throws Exception {
        String sessionId = extractParam(wsSession, "sessionId");
        if (sessionId == null) return;

        TerminalSession termSession = sessionRegistry.findById(sessionId).orElse(null);
        if (termSession == null || termSession.getStatus() != TerminalSessionStatus.ACTIVE) {
            sendText(wsSession, "{\"type\":\"error\",\"message\":\"Session closed\"}");
            return;
        }

        Map<?, ?> payload = objectMapper.readValue(message.getPayload(), Map.class);
        String command = (String) payload.get("command");

        if (command == null || command.isBlank()) return;

        // Handle cd client-side in the session without a subprocess
        if (command.trim().startsWith("cd ")) {
            String target = command.trim().substring(3).trim();
            File newDir = target.startsWith("/") ? new File(target) : new File(termSession.getWorkingDirectory(), target);
            if (newDir.isDirectory()) {
                termSession.setWorkingDirectory(newDir.getCanonicalPath());
                termSession.touch();
                termSession.addToHistory(command);
                sendText(wsSession, "{\"type\":\"cwd\",\"cwd\":\"" + newDir.getCanonicalPath() + "\"}");
            } else {
                sendText(wsSession, "{\"type\":\"output\",\"data\":\"cd: no such directory: " + target + "\\n\"}");
            }
            return;
        }

        // Kill any existing running process for this WS session
        Process existing = activeProcesses.remove(wsSession.getId());
        if (existing != null && existing.isAlive()) existing.destroyForcibly();

        termSession.addToHistory(command);
        termSession.touch();

        // Stream subprocess output asynchronously
        Thread.ofVirtual().start(() -> {
            try {
                Process process = new ProcessBuilder("/bin/sh", "-c", command)
                        .directory(new File(termSession.getWorkingDirectory()))
                        .redirectErrorStream(true)
                        .start();

                activeProcesses.put(wsSession.getId(), process);

                try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        if (wsSession.isOpen()) {
                            String jsonLine = objectMapper.writeValueAsString(
                                    Map.of("type", "output", "data", line + "\n"));
                            wsSession.sendMessage(new TextMessage(jsonLine));
                        } else break;
                    }
                }

                int exitCode = process.waitFor();
                activeProcesses.remove(wsSession.getId());
                if (wsSession.isOpen()) {
                    String done = objectMapper.writeValueAsString(
                            Map.of("type", "done", "exitCode", exitCode, "cwd", termSession.getWorkingDirectory()));
                    wsSession.sendMessage(new TextMessage(done));
                }
            } catch (Exception e) {
                sendText(wsSession, "{\"type\":\"error\",\"message\":\"" + e.getMessage() + "\"}");
            }
        });
    }

    @Override
    public void afterConnectionClosed(WebSocketSession wsSession, CloseStatus status) {
        Process p = activeProcesses.remove(wsSession.getId());
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
}
