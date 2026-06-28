package com.forge.observability.log;

import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/** Filters a list of {@link LogEntry} by level, keyword search, and timestamp range. */
public class LogFilter {

    private final Set<LogLevel> levels;
    private final String searchText;
    private final boolean caseSensitive;
    private final boolean useRegex;
    private final Instant from;
    private final Instant to;

    private LogFilter(Builder b) {
        this.levels = b.levels;
        this.searchText = b.searchText;
        this.caseSensitive = b.caseSensitive;
        this.useRegex = b.useRegex;
        this.from = b.from;
        this.to = b.to;
    }

    public List<LogEntry> apply(List<LogEntry> entries) {
        Pattern compiled = (searchText != null && useRegex)
                ? Pattern.compile(searchText, caseSensitive ? 0 : Pattern.CASE_INSENSITIVE)
                : null;

        return entries.stream().filter(entry -> {
            if (levels != null && !levels.isEmpty() && !levels.contains(entry.getLevel())) return false;
            if (from != null && entry.getTimestamp() != null && entry.getTimestamp().isBefore(from)) return false;
            if (to != null && entry.getTimestamp() != null && entry.getTimestamp().isAfter(to)) return false;
            if (searchText != null && !searchText.isBlank()) {
                String line = entry.getRawLine();
                if (compiled != null) {
                    if (!compiled.matcher(line).find()) return false;
                } else {
                    String haystack = caseSensitive ? line : line.toLowerCase();
                    String needle = caseSensitive ? searchText : searchText.toLowerCase();
                    if (!haystack.contains(needle)) return false;
                }
            }
            return true;
        }).collect(Collectors.toList());
    }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Set<LogLevel> levels;
        private String searchText;
        private boolean caseSensitive = false;
        private boolean useRegex = false;
        private Instant from;
        private Instant to;

        public Builder levels(Set<LogLevel> levels) { this.levels = levels; return this; }
        public Builder search(String text) { this.searchText = text; return this; }
        public Builder caseSensitive(boolean cs) { this.caseSensitive = cs; return this; }
        public Builder regex(boolean re) { this.useRegex = re; return this; }
        public Builder from(Instant from) { this.from = from; return this; }
        public Builder to(Instant to) { this.to = to; return this; }
        public LogFilter build() { return new LogFilter(this); }
    }
}
