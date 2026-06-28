package com.forge.devopstoolbox.api.dto;

import java.time.Instant;
import java.util.UUID;

public record JobResponse(
        UUID id,
        String toolId,
        String status,
        Instant startedAt,
        Instant finishedAt,
        Long durationMs,
        String output,
        String errorMessage,
        Instant createdAt
) {
}
