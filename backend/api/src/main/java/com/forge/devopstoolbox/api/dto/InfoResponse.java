package com.forge.devopstoolbox.api.dto;

public record InfoResponse(
        String name,
        String description,
        String environment,
        String buildTime
) {
}
