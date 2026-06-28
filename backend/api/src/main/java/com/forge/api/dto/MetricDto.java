package com.forge.api.dto;

import java.time.Instant;

public record MetricDto(
    String id,
    String metricName,
    Double metricValue,
    String dimensions,
    Instant timestamp
) {}
