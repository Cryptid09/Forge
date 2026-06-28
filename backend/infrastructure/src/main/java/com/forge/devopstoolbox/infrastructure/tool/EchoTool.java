package com.forge.devopstoolbox.infrastructure.tool;

import com.forge.devopstoolbox.core.tool.Tool;
import com.forge.devopstoolbox.core.tool.ToolContext;
import com.forge.devopstoolbox.core.tool.ToolDescriptor;
import com.forge.devopstoolbox.core.tool.ToolResult;
import org.springframework.stereotype.Component;

@Component
public class EchoTool implements Tool {

    private static final ToolDescriptor DESCRIPTOR = new ToolDescriptor(
            "echo",
            "Echo Tool",
            "Returns the input message unchanged. Used to verify the execution pipeline.",
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
        String message = context.parameters().getOrDefault("message", "");
        long durationMs = (System.nanoTime() - start) / 1_000_000;
        return ToolResult.success(message, durationMs);
    }
}
