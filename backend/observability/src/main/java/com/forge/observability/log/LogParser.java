package com.forge.observability.log;

import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Parses raw log lines into structured {@link LogEntry} objects.
 *
 * Supports common log formats:
 *  - Spring Boot:  2024-01-01T12:00:00Z  INFO  --- [thread] Class : message
 *  - Docker JSON:  {"log":"...", "stream":"stdout", "time":"..."}
 *  - Plain text with level keywords:  [ERROR] message
 *  - Fallback: level detected by scanning for level keywords anywhere in the line
 */
@Component
public class LogParser {

    // Spring Boot / SLF4J pattern: timestamp LEVEL  --- [thread] logger : msg
    private static final Pattern SPRING_PATTERN = Pattern.compile(
            "(\\d{4}-\\d{2}-\\d{2}[T ]\\d{2}:\\d{2}:\\d{2}\\.?\\d*Z?)\\s+(TRACE|DEBUG|INFO|WARN|ERROR)\\s+.*");

    // ISO timestamp without level
    private static final Pattern TIMESTAMP_PATTERN = Pattern.compile(
            "^(\\d{4}-\\d{2}-\\d{2}[T ]\\d{2}:\\d{2}:\\d{2})");

    public List<LogEntry> parse(String rawText) {
        List<LogEntry> entries = new ArrayList<>();
        if (rawText == null || rawText.isBlank()) return entries;

        String[] lines = rawText.split("\n");
        for (int i = 0; i < lines.length; i++) {
            entries.add(parseLine(i + 1, lines[i]));
        }
        return entries;
    }

    public LogEntry parseLine(int lineNumber, String rawLine) {
        Instant timestamp = null;
        LogLevel level = LogLevel.UNKNOWN;
        String message = rawLine;

        Matcher springMatcher = SPRING_PATTERN.matcher(rawLine);
        if (springMatcher.matches()) {
            try { timestamp = Instant.parse(springMatcher.group(1).replace(" ", "T") + (springMatcher.group(1).endsWith("Z") ? "" : "Z")); } catch (DateTimeParseException ignored) {}
            level = parseLevel(springMatcher.group(2));
        } else {
            Matcher tsMatcher = TIMESTAMP_PATTERN.matcher(rawLine);
            if (tsMatcher.find()) {
                try { timestamp = Instant.parse(tsMatcher.group(1) + ":00Z"); } catch (DateTimeParseException ignored) {}
            }
            level = detectLevel(rawLine);
        }

        return new LogEntry(lineNumber, timestamp, level, null, message, rawLine);
    }

    private LogLevel detectLevel(String line) {
        String upper = line.toUpperCase();
        if (upper.contains(" ERROR") || upper.contains("[ERROR]") || upper.contains("SEVERE") || upper.contains("EXCEPTION")) return LogLevel.ERROR;
        if (upper.contains(" WARN") || upper.contains("[WARN]") || upper.contains("WARNING")) return LogLevel.WARN;
        if (upper.contains(" INFO") || upper.contains("[INFO]")) return LogLevel.INFO;
        if (upper.contains(" DEBUG") || upper.contains("[DEBUG]")) return LogLevel.DEBUG;
        if (upper.contains(" TRACE") || upper.contains("[TRACE]")) return LogLevel.TRACE;
        return LogLevel.UNKNOWN;
    }

    private LogLevel parseLevel(String s) {
        try { return LogLevel.valueOf(s.toUpperCase()); } catch (Exception e) { return LogLevel.UNKNOWN; }
    }
}
