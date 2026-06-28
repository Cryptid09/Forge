package com.forge.devopstoolbox.core.model;

import java.time.Instant;
import java.util.UUID;

public record PlatformEvent(
        UUID id,
        EventType eventType,
        UUID jobId,
        String toolId,
        Instant timestamp,
        String payload
) {
}
