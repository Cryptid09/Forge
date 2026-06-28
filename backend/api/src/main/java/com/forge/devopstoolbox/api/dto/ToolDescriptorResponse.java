package com.forge.devopstoolbox.api.dto;

public record ToolDescriptorResponse(
        String id,
        String name,
        String description,
        String category,
        String version
) {
}
