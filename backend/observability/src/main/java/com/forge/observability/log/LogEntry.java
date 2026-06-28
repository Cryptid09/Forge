package com.forge.observability.log;

import java.time.Instant;

public class LogEntry {
    private final int lineNumber;
    private final Instant timestamp;
    private final LogLevel level;
    private final String source;
    private final String message;
    private final String rawLine;

    public LogEntry(int lineNumber, Instant timestamp, LogLevel level, String source, String message, String rawLine) {
        this.lineNumber = lineNumber;
        this.timestamp = timestamp;
        this.level = level;
        this.source = source;
        this.message = message;
        this.rawLine = rawLine;
    }

    public int getLineNumber() { return lineNumber; }
    public Instant getTimestamp() { return timestamp; }
    public LogLevel getLevel() { return level; }
    public String getSource() { return source; }
    public String getMessage() { return message; }
    public String getRawLine() { return rawLine; }
}
