package com.forge.infrastructure.tool.impl;

import com.forge.core.tool.Tool;
import com.forge.core.tool.ToolContext;
import com.forge.core.tool.ToolDescriptor;
import com.forge.core.tool.ToolResult;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
public class EchoTool implements Tool {

    private final ToolDescriptor descriptor = new ToolDescriptor(
            "echo-tool",
            "Echo Tool",
            "Echoes the provided message",
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
        String message = context.parameters().getOrDefault("message", "Hello DevOps");
        
        return new ToolResult(
                ToolResult.ExecutionStatus.SUCCESS,
                message,
                null,
                Duration.ofMillis(System.currentTimeMillis() - start),
                0
        );
    }
}
