package com.forge.observability.terminal;

import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class TerminalSession {
    private final String sessionId;
    private TerminalSessionStatus status;
    private String workingDirectory;
    private final Map<String, String> environment;
    private final Instant createdAt;
    private Instant lastActivity;
    private final List<String> commandHistory;

    public TerminalSession(String sessionId, String workingDirectory) {
        this.sessionId = sessionId;
        this.status = TerminalSessionStatus.ACTIVE;
        this.workingDirectory = workingDirectory;
        this.environment = new LinkedHashMap<>(System.getenv());
        this.createdAt = Instant.now();
        this.lastActivity = Instant.now();
        this.commandHistory = new ArrayList<>();
    }

    public String getSessionId() { return sessionId; }

    public TerminalSessionStatus getStatus() { return status; }
    public void setStatus(TerminalSessionStatus status) { this.status = status; }

    public String getWorkingDirectory() { return workingDirectory; }
    public void setWorkingDirectory(String workingDirectory) { this.workingDirectory = workingDirectory; }

    public Map<String, String> getEnvironment() { return environment; }

    public Instant getCreatedAt() { return createdAt; }

    public Instant getLastActivity() { return lastActivity; }
    public void touch() { this.lastActivity = Instant.now(); }

    public List<String> getCommandHistory() { return commandHistory; }
    public void addToHistory(String command) { commandHistory.add(command); }
}
