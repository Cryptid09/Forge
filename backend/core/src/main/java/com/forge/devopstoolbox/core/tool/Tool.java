package com.forge.devopstoolbox.core.tool;

public interface Tool {

    ToolDescriptor descriptor();

    ToolResult execute(ToolContext context);
}
