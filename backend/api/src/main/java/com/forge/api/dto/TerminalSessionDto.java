package com.forge.api.dto;

import java.time.Instant;
import java.util.List;

public record TerminalSessionDto(
        String sessionId,
        String status,
        String workingDirectory,
        Instant createdAt,
        Instant lastActivity,
        List<String> commandHistory
) {}

