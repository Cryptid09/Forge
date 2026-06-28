package com.forge.observability.analyzer;

import java.time.Instant;
import java.util.List;
import java.util.Map;

public class AnalysisResult {

    public enum Severity { LOW, MEDIUM, HIGH, CRITICAL }

    private final String summary;
    private final Severity severity;
    private final int totalLines;
    private final int errorCount;
    private final int warnCount;
    private final List<Finding> findings;
    private final String suggestedRootCause;
    private final List<TimelineEvent> timeline;
    private final Instant analyzedAt;

    public AnalysisResult(String summary, Severity severity, int totalLines,
                          int errorCount, int warnCount, List<Finding> findings,
                          String suggestedRootCause, List<TimelineEvent> timeline) {
        this.summary = summary;
        this.severity = severity;
        this.totalLines = totalLines;
        this.errorCount = errorCount;
        this.warnCount = warnCount;
        this.findings = findings;
        this.suggestedRootCause = suggestedRootCause;
        this.timeline = timeline;
        this.analyzedAt = Instant.now();
    }

    public String getSummary() { return summary; }
    public Severity getSeverity() { return severity; }
    public int getTotalLines() { return totalLines; }
    public int getErrorCount() { return errorCount; }
    public int getWarnCount() { return warnCount; }
    public List<Finding> getFindings() { return findings; }
    public String getSuggestedRootCause() { return suggestedRootCause; }
    public List<TimelineEvent> getTimeline() { return timeline; }
    public Instant getAnalyzedAt() { return analyzedAt; }

    // ── Nested types ──────────────────────────────────────────────────────────

    public record Finding(
            String patternName,
            Severity severity,
            int lineNumber,
            String excerpt,
            int occurrences
    ) {}

    public record TimelineEvent(
            int lineNumber,
            Instant timestamp,
            String level,
            String message
    ) {}
}
