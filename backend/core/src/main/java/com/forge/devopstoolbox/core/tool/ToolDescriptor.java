package com.forge.devopstoolbox.core.tool;

public record ToolDescriptor(
        String id,
        String name,
        String description,
        String category,
        String version
) {
}
