package com.forge.core.tool;

public record ToolDescriptor(
    String id,
    String name,
    String description,
    String category,
    String version
) {}
