package com.forge.api.dto;

import java.time.Instant;
import java.util.List;
import java.util.Map;

public record AnalysisResultDto(
        String summary,
        String severity,
        int totalLines,
        int errorCount,
        int warnCount,
        List<FindingDto> findings,
        String suggestedRootCause,
        List<TimelineEventDto> timeline,
        Instant analyzedAt
) {
    public record FindingDto(String patternName, String severity, int lineNumber, String excerpt, int occurrences) {}
    public record TimelineEventDto(int lineNumber, Instant timestamp, String level, String message) {}
}
