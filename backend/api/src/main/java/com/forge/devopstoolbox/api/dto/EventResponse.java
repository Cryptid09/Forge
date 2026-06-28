package com.forge.devopstoolbox.api.dto;

import java.time.Instant;
import java.util.UUID;

public record EventResponse(
        UUID id,
        String eventType,
        UUID jobId,
        String toolId,
        Instant timestamp,
        String payload
) {
}
