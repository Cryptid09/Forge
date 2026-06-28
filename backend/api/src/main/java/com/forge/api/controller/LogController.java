package com.forge.api.controller;

import com.forge.api.dto.AnalysisResultDto;
import com.forge.application.execution.ExecutionService;
import com.forge.core.job.Job;
import com.forge.observability.analyzer.AnalysisResult;
import com.forge.observability.analyzer.LogAnalyzer;
import com.forge.observability.log.LogEntry;
import com.forge.observability.log.LogFilter;
import com.forge.observability.log.LogLevel;
import com.forge.observability.log.LogParser;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/logs")
@CrossOrigin("*")
public class LogController {

    private final LogAnalyzer logAnalyzer;
    private final LogParser logParser;
    private final ExecutionService executionService;

    public LogController(LogAnalyzer logAnalyzer, LogParser logParser, ExecutionService executionService) {
        this.logAnalyzer = logAnalyzer;
        this.logParser = logParser;
        this.executionService = executionService;
    }

    /**
     * Parse + filter raw logs.
     * Query params: search, levels (comma-separated), caseSensitive, regex
     */
    @PostMapping
    public ResponseEntity<List<Map<String, Object>>> parseAndFilter(
            @RequestBody Map<String, String> body,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String levels,
            @RequestParam(defaultValue = "false") boolean caseSensitive,
            @RequestParam(defaultValue = "false") boolean regex) {

        String rawLogs = body.get("logs");
        if (rawLogs == null) return ResponseEntity.badRequest().build();

        List<LogEntry> entries = logParser.parse(rawLogs);

        Set<LogLevel> levelFilter = null;
        if (levels != null && !levels.isBlank()) {
            levelFilter = Set.of(levels.split(",")).stream()
                    .map(String::trim).map(String::toUpperCase)
                    .map(l -> { try { return LogLevel.valueOf(l); } catch (Exception e) { return null; } })
                    .filter(l -> l != null)
                    .collect(Collectors.toSet());
        }

        LogFilter filter = LogFilter.builder()
                .search(search)
                .levels(levelFilter)
                .caseSensitive(caseSensitive)
                .regex(regex)
                .build();

        List<LogEntry> filtered = filter.apply(entries);

        List<Map<String, Object>> result = filtered.stream().map(e -> Map.<String, Object>of(
                "lineNumber", e.getLineNumber(),
                "level", e.getLevel().name(),
                "message", e.getMessage(),
                "timestamp", e.getTimestamp() != null ? e.getTimestamp().toString() : ""
        )).toList();

        return ResponseEntity.ok(result);
    }

    /**
     * POST /api/logs/analyze — run LogAnalyzer as a Tool, creating a Job.
     * Body: { "logs": "...", "source": "optional label" }
     */
    @PostMapping("/analyze")
    public ResponseEntity<?> analyze(@RequestBody Map<String, String> body) {
        String rawLogs = body.get("logs");
        if (rawLogs == null || rawLogs.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("error", "'logs' is required"));
        }

        // Execute via Tool Runtime so a Job is created and event is published
        Job job = executionService.executeToolSync("analyze-logs",
                Map.of("logs", rawLogs, "source", body.getOrDefault("source", "manual")));

        // Also return the parsed result directly for the UI
        AnalysisResult result = logAnalyzer.analyze(rawLogs);
        return ResponseEntity.ok(Map.of(
                "jobId", job.getId(),
                "status", job.getStatus().name(),
                "analysis", toDto(result)
        ));
    }

    private AnalysisResultDto toDto(AnalysisResult r) {
        return new AnalysisResultDto(
                r.getSummary(), r.getSeverity().name(),
                r.getTotalLines(), r.getErrorCount(), r.getWarnCount(),
                r.getFindings().stream().map(f -> new AnalysisResultDto.FindingDto(
                        f.patternName(), f.severity().name(), f.lineNumber(), f.excerpt(), f.occurrences()
                )).toList(),
                r.getSuggestedRootCause(),
                r.getTimeline().stream().map(t -> new AnalysisResultDto.TimelineEventDto(
                        t.lineNumber(), t.timestamp(), t.level(), t.message()
                )).toList(),
                r.getAnalyzedAt()
        );
    }
}
