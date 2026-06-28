package com.forge.devopstoolbox.core.model;

import java.time.Instant;
import java.util.UUID;

public record Job(
        UUID id,
        String toolId,
        JobStatus status,
        Instant startedAt,
        Instant finishedAt,
        Long durationMs,
        String output,
        String errorMessage,
        Instant createdAt
) {

    public static Job queued(UUID id, String toolId, Instant createdAt) {
        return new Job(id, toolId, JobStatus.QUEUED, null, null, null, null, null, createdAt);
    }
}
