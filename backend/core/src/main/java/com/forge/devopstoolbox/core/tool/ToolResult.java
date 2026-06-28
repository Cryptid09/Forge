package com.forge.devopstoolbox.core.tool;

public record ToolResult(
        ExecutionStatus status,
        String outputMessage,
        String errorMessage,
        long durationMs,
        Integer exitCode
) {

    public enum ExecutionStatus {
        SUCCESS,
        FAILURE
    }

    public static ToolResult success(String output, long durationMs) {
        return new ToolResult(ExecutionStatus.SUCCESS, output, null, durationMs, 0);
    }

    public static ToolResult failure(String error, long durationMs, Integer exitCode) {
        return new ToolResult(ExecutionStatus.FAILURE, null, error, durationMs, exitCode);
    }
}
