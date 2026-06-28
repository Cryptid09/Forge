package com.forge.core.tool;

public interface Tool {
    ToolDescriptor getDescriptor();
    ToolResult execute(ToolContext context);
}
