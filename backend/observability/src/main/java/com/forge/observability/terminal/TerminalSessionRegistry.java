package com.forge.observability.terminal;

import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class TerminalSessionRegistry {

    private final ConcurrentHashMap<String, TerminalSession> sessions = new ConcurrentHashMap<>();

    public TerminalSession createSession(String workingDirectory) {
        String sessionId = UUID.randomUUID().toString();
        String cwd = (workingDirectory != null && !workingDirectory.isBlank())
                ? workingDirectory
                : System.getProperty("user.home");
        TerminalSession session = new TerminalSession(sessionId, cwd);
        sessions.put(sessionId, session);
        return session;
    }

    public Optional<TerminalSession> findById(String sessionId) {
        return Optional.ofNullable(sessions.get(sessionId));
    }

    public Collection<TerminalSession> listActive() {
        return sessions.values().stream()
                .filter(s -> s.getStatus() == TerminalSessionStatus.ACTIVE)
                .toList();
    }

    public void closeSession(String sessionId) {
        TerminalSession session = sessions.get(sessionId);
        if (session != null) {
            session.setStatus(TerminalSessionStatus.CLOSED);
            sessions.remove(sessionId);
        }
    }
}
