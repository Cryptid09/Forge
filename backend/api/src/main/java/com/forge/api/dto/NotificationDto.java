package com.forge.api.dto;

import java.time.Instant;

public record NotificationDto(
    String id,
    String message,
    String source,
    String severity,
    boolean read,
    Instant timestamp
) {}
