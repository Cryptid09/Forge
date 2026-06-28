package com.forge.core.tool;

import java.time.Instant;
import java.util.Map;

public record ToolContext(
    Map<String, String> parameters,
    String executionDirectory,
    Map<String, String> environmentVariables,
    Instant timestamp,
    String userContext
) {}
