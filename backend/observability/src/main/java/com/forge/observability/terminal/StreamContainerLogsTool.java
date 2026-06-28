package com.forge.observability.terminal;

import com.forge.core.tool.Tool;
import com.forge.core.tool.ToolContext;
import com.forge.core.tool.ToolResult;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * Tool: stream-container-logs
 *
 * Parameters:
 *   containerId – Docker container ID or name (required)
 *   tail        – number of lines to fetch from the end (default 100)
 *   follow      – "true" to follow live (default false; live streaming done via WebSocket handler)
 *
 * In live-streaming scenarios, the WebSocket handler calls docker logs -f directly
 * as a subprocess, not through this Tool. This Tool handles historical/snapshot fetching
 * and creates the Job record for tracking.
 */
@Component
public class StreamContainerLogsTool implements Tool {

    @Override
    public com.forge.core.tool.ToolDescriptor getDescriptor() {
        return new com.forge.core.tool.ToolDescriptor(
            "stream-container-logs",
            "Stream Container Logs",
            "Fetches logs from a Docker container.",
            "Observability",
            "1.0"
        );
    }

    @Override
    public ToolResult execute(ToolContext context) {
        Instant start = Instant.now();
        String containerId = context.parameters().get("containerId");
        String tail = context.parameters().getOrDefault("tail", "100");

        if (containerId == null || containerId.isBlank()) {
            return new ToolResult(ToolResult.ExecutionStatus.FAILURE, null, "'containerId' parameter is required", Duration.ZERO, -1);
        }

        try {
            Process process = new ProcessBuilder(
                    "docker", "logs", "--tail", tail, containerId)
                    .redirectErrorStream(true)
                    .start();

            StringBuilder output = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    output.append(line).append("\n");
                }
            }

            process.waitFor(10, TimeUnit.SECONDS);
            Duration duration = Duration.between(start, Instant.now());

            return new ToolResult(ToolResult.ExecutionStatus.SUCCESS, output.toString(), null, duration, 0);

        } catch (Exception e) {
            return new ToolResult(ToolResult.ExecutionStatus.FAILURE, null, "Failed to fetch container logs: " + e.getMessage(), Duration.between(start, Instant.now()), -1);
        }
    }
}
