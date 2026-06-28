package com.forge.devopstoolbox.core.tool;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

public record ToolContext(
        UUID jobId,
        Map<String, String> parameters,
        String executionDirectory,
        Map<String, String> environmentVariables,
        Instant timestamp,
        String userId
) {

    public static ToolContext create(UUID jobId, Map<String, String> parameters) {
        return new ToolContext(jobId, parameters, null, Map.of(), Instant.now(), null);
    }
}
