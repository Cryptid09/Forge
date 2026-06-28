package com.forge.core.tool;

import java.time.Duration;

public record ToolResult(
    ExecutionStatus executionStatus,
    String outputMessage,
    String errorMessage,
    Duration executionDuration,
    Integer exitCode
) {
    public enum ExecutionStatus {
        SUCCESS,
        FAILURE
    }
}
