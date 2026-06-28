package com.forge.observability.analyzer;

import com.forge.observability.log.LogEntry;
import com.forge.observability.log.LogLevel;
import com.forge.observability.log.LogParser;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.regex.Pattern;

/**
 * Pattern-based log analysis engine.
 *
 * <p>Each registered pattern matcher is applied to every log line.
 * Adding a new pattern only requires adding an entry to {@code PATTERNS} —
 * the analysis loop never changes.
 *
 * <h3>Detected patterns</h3>
 * NullPointerException, OutOfMemoryError, ConnectException, SocketTimeoutException,
 * Address already in use, Spring Boot startup failure, generic Exception in thread,
 * stack trace lines, WARN/ERROR level, repeated identical errors.
 */
@Component
public class LogAnalyzer {

    private final LogParser logParser;

    public LogAnalyzer(LogParser logParser) {
        this.logParser = logParser;
    }

    // ── Pattern registry ─────────────────────────────────────────────────────

    private record PatternSpec(
            String name,
            Pattern pattern,
            AnalysisResult.Severity severity
    ) {}

    private static final List<PatternSpec> PATTERNS = List.of(
            new PatternSpec("OutOfMemoryError",             p("java\\.lang\\.OutOfMemoryError"),                  AnalysisResult.Severity.CRITICAL),
            new PatternSpec("ApplicationFailedToStart",     p("APPLICATION FAILED TO START"),                     AnalysisResult.Severity.CRITICAL),
            new PatternSpec("NullPointerException",         p("java\\.lang\\.NullPointerException"),              AnalysisResult.Severity.HIGH),
            new PatternSpec("ExceptionInThread",            p("Exception in thread"),                             AnalysisResult.Severity.HIGH),
            new PatternSpec("ConnectException",             p("java\\.net\\.ConnectException|Connection refused"), AnalysisResult.Severity.HIGH),
            new PatternSpec("AddressAlreadyInUse",          p("Address already in use"),                          AnalysisResult.Severity.HIGH),
            new PatternSpec("SocketTimeoutException",       p("java\\.net\\.SocketTimeoutException|timed out"),   AnalysisResult.Severity.MEDIUM),
            new PatternSpec("ClassNotFoundException",       p("java\\.lang\\.ClassNotFoundException"),            AnalysisResult.Severity.MEDIUM),
            new PatternSpec("BeanCreationException",        p("BeanCreationException|Error creating bean"),       AnalysisResult.Severity.HIGH),
            new PatternSpec("DataAccessException",          p("DataAccessException|Unable to acquire JDBC"),      AnalysisResult.Severity.HIGH),
            new PatternSpec("IllegalArgumentException",     p("java\\.lang\\.IllegalArgumentException"),          AnalysisResult.Severity.MEDIUM),
            new PatternSpec("IllegalStateException",        p("java\\.lang\\.IllegalStateException"),             AnalysisResult.Severity.MEDIUM),
            new PatternSpec("GenericException",             p("Exception:|Caused by:"),                           AnalysisResult.Severity.MEDIUM),
            new PatternSpec("ErrorLogLevel",                p("\\bERROR\\b|\\bSEVERE\\b"),                        AnalysisResult.Severity.MEDIUM),
            new PatternSpec("WarnLogLevel",                 p("\\bWARN\\b|\\bWARNING\\b"),                        AnalysisResult.Severity.LOW)
    );

    private static Pattern p(String regex) {
        return Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
    }

    // ── Analysis ─────────────────────────────────────────────────────────────

    public AnalysisResult analyze(String rawLogs) {
        List<LogEntry> entries = logParser.parse(rawLogs);
        return analyzeEntries(entries);
    }

    public AnalysisResult analyzeEntries(List<LogEntry> entries) {
        Map<String, List<AnalysisResult.Finding>> grouped = new LinkedHashMap<>();
        Map<String, Integer> occurrences = new LinkedHashMap<>();
        List<AnalysisResult.TimelineEvent> timeline = new ArrayList<>();
        int errorCount = 0;
        int warnCount = 0;

        for (LogEntry entry : entries) {
            if (entry.getLevel() == LogLevel.ERROR) errorCount++;
            if (entry.getLevel() == LogLevel.WARN) warnCount++;

            if (entry.getTimestamp() != null && (entry.getLevel() == LogLevel.ERROR || entry.getLevel() == LogLevel.WARN)) {
                timeline.add(new AnalysisResult.TimelineEvent(
                        entry.getLineNumber(), entry.getTimestamp(),
                        entry.getLevel().name(), truncate(entry.getMessage(), 120)));
            }

            String line = entry.getRawLine();
            for (PatternSpec spec : PATTERNS) {
                if (spec.pattern().matcher(line).find()) {
                    occurrences.merge(spec.name(), 1, Integer::sum);
                    grouped.computeIfAbsent(spec.name(), k -> new ArrayList<>())
                            .add(new AnalysisResult.Finding(
                                    spec.name(), spec.severity(),
                                    entry.getLineNumber(),
                                    truncate(line.strip(), 200),
                                    1));
                    break; // one finding per line (highest-priority pattern wins)
                }
            }
        }

        // Collapse grouped findings into one per pattern with total occurrence count
        List<AnalysisResult.Finding> findings = grouped.entrySet().stream()
                .map(e -> {
                    List<AnalysisResult.Finding> group = e.getValue();
                    AnalysisResult.Finding first = group.get(0);
                    return new AnalysisResult.Finding(
                            first.patternName(), first.severity(),
                            first.lineNumber(), first.excerpt(),
                            occurrences.get(e.getKey()));
                })
                .sorted(Comparator.comparing(f -> f.severity().ordinal() * -1)) // highest severity first
                .toList();

        AnalysisResult.Severity overallSeverity = computeOverallSeverity(findings);
        String rootCause = suggestRootCause(findings);
        String summary = buildSummary(entries.size(), errorCount, warnCount, findings);

        return new AnalysisResult(summary, overallSeverity, entries.size(),
                errorCount, warnCount, findings, rootCause, timeline);
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private AnalysisResult.Severity computeOverallSeverity(List<AnalysisResult.Finding> findings) {
        return findings.stream()
                .map(AnalysisResult.Finding::severity)
                .max(Comparator.comparingInt(Enum::ordinal))
                .orElse(AnalysisResult.Severity.LOW);
    }

    private String suggestRootCause(List<AnalysisResult.Finding> findings) {
        if (findings.isEmpty()) return "No significant issues detected.";
        // Pick the highest-severity finding
        AnalysisResult.Finding top = findings.get(0);
        return switch (top.patternName()) {
            case "OutOfMemoryError"         -> "JVM heap exhausted. Review memory settings (-Xmx) and check for memory leaks.";
            case "ApplicationFailedToStart" -> "Spring Boot failed to start. Check bean configuration and property sources.";
            case "NullPointerException"     -> "Null reference encountered. Review stack trace for the offending call site.";
            case "ConnectException"         -> "Network connection refused. Verify target service is running and reachable.";
            case "AddressAlreadyInUse"      -> "Port already bound. Stop the conflicting process or change the server port.";
            case "SocketTimeoutException"   -> "Network timeout detected. Check target service latency and timeout configuration.";
            case "BeanCreationException"    -> "Spring bean wiring failed. Review application context configuration.";
            case "DataAccessException"      -> "Database access failure. Check connection pool and database availability.";
            default -> "Review the top findings — " + top.patternName() + " detected " + top.occurrences() + " time(s).";
        };
    }

    private String buildSummary(int totalLines, int errors, int warns, List<AnalysisResult.Finding> findings) {
        if (findings.isEmpty()) return "Analysis complete — no significant issues found in " + totalLines + " lines.";
        return String.format("Analysed %d lines: %d error(s), %d warning(s), %d distinct issue pattern(s) detected.",
                totalLines, errors, warns, findings.size());
    }

    private String truncate(String s, int max) {
        return s.length() <= max ? s : s.substring(0, max) + "…";
    }
}
