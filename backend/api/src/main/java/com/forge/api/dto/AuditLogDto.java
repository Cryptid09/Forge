package com.forge.api.dto;

import java.time.Instant;

public record AuditLogDto(
    String id,
    String eventType,
    String source,
    String correlationId,
    String workflowId,
    String jobId,
    String payload,
    int version,
    Instant timestamp
) {}
