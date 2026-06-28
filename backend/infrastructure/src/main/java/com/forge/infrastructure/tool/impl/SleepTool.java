package com.forge.infrastructure.tool.impl;

import com.forge.core.tool.Tool;
import com.forge.core.tool.ToolContext;
import com.forge.core.tool.ToolDescriptor;
import com.forge.core.tool.ToolResult;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
public class SleepTool implements Tool {

    private final ToolDescriptor descriptor = new ToolDescriptor(
            "sleep-tool",
            "Sleep Tool",
            "Waits for a specified duration",
            "Demonstration",
            "1.0.0"
    );

    @Override
    public ToolDescriptor getDescriptor() {
        return descriptor;
    }

    @Override
    public ToolResult execute(ToolContext context) {
        long start = System.currentTimeMillis();
        long durationMs = Long.parseLong(context.parameters().getOrDefault("durationMs", "5000"));
        
        try {
            Thread.sleep(durationMs);
            return new ToolResult(
                    ToolResult.ExecutionStatus.SUCCESS,
                    "Slept for " + durationMs + " ms",
                    null,
                    Duration.ofMillis(System.currentTimeMillis() - start),
                    0
            );
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return new ToolResult(
                    ToolResult.ExecutionStatus.FAILURE,
                    null,
                    "Sleep interrupted: " + e.getMessage(),
                    Duration.ofMillis(System.currentTimeMillis() - start),
                    1
            );
        }
    }
}
