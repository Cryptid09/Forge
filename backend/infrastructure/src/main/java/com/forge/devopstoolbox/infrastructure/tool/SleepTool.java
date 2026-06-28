package com.forge.devopstoolbox.infrastructure.tool;

import com.forge.devopstoolbox.core.tool.Tool;
import com.forge.devopstoolbox.core.tool.ToolContext;
import com.forge.devopstoolbox.core.tool.ToolDescriptor;
import com.forge.devopstoolbox.core.tool.ToolResult;
import org.springframework.stereotype.Component;

@Component
public class SleepTool implements Tool {

    private static final ToolDescriptor DESCRIPTOR = new ToolDescriptor(
            "sleep",
            "Sleep Tool",
            "Waits for a specified duration. Used to verify long-running job execution.",
            "demonstration",
            "1.0.0"
    );

    @Override
    public ToolDescriptor descriptor() {
        return DESCRIPTOR;
    }

    @Override
    public ToolResult execute(ToolContext context) {
        long start = System.nanoTime();
        String durationValue = context.parameters().getOrDefault("durationSeconds", "1");
        int durationSeconds;
        try {
            durationSeconds = Integer.parseInt(durationValue);
        } catch (NumberFormatException exception) {
            long durationMs = (System.nanoTime() - start) / 1_000_000;
            return ToolResult.failure("Invalid durationSeconds parameter", durationMs, 1);
        }

        if (durationSeconds < 0 || durationSeconds > 300) {
            long durationMs = (System.nanoTime() - start) / 1_000_000;
            return ToolResult.failure("durationSeconds must be between 0 and 300", durationMs, 1);
        }

        try {
            Thread.sleep(durationSeconds * 1000L);
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            long durationMs = (System.nanoTime() - start) / 1_000_000;
            return ToolResult.failure("Sleep interrupted", durationMs, 1);
        }

        long durationMs = (System.nanoTime() - start) / 1_000_000;
        return ToolResult.success("Slept for " + durationSeconds + " seconds", durationMs);
    }
}
