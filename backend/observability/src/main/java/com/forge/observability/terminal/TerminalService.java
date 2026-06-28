package com.forge.observability.terminal;

import com.forge.application.execution.ExecutionService;
import com.forge.core.event.Event;
import com.forge.core.event.EventBus;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Map;

@Service
public class TerminalService {

    private final TerminalSessionRegistry registry;
    private final ExecutionService executionService;
    private final EventBus eventBus;

    public TerminalService(TerminalSessionRegistry registry,
                           ExecutionService executionService,
                           EventBus eventBus) {
        this.registry = registry;
        this.executionService = executionService;
        this.eventBus = eventBus;
    }

    public TerminalSession createSession(String workingDirectory) {
        TerminalSession session = registry.createSession(workingDirectory);
        eventBus.publish(new Event("TerminalSessionStarted", null,
                "Terminal session created: " + session.getSessionId()));
        return session;
    }

    public void closeSession(String sessionId) {
        registry.findById(sessionId).ifPresent(s -> {
            registry.closeSession(sessionId);
            eventBus.publish(new Event("TerminalSessionClosed", null,
                    "Terminal session closed: " + sessionId));
        });
    }

    /** Executes a command through the Tool Runtime, creating a tracked Job. */
    public com.forge.core.job.Job executeCommand(String sessionId, String command) {
        TerminalSession session = registry.findById(sessionId)
                .orElseThrow(() -> new IllegalArgumentException("Session not found: " + sessionId));

        if (session.getStatus() != TerminalSessionStatus.ACTIVE) {
            throw new IllegalStateException("Session " + sessionId + " is closed");
        }

        var job = executionService.executeToolSync("execute-command",
                Map.of("sessionId", sessionId, "command", command));

        eventBus.publish(new Event("CommandExecuted", job.getId(),
                "Command executed in session " + sessionId + ": " + command));

        return job;
    }

    public TerminalSession getSession(String sessionId) {
        return registry.findById(sessionId)
                .orElseThrow(() -> new IllegalArgumentException("Session not found: " + sessionId));
    }

    public Collection<TerminalSession> listActiveSessions() {
        return registry.listActive();
    }
}
