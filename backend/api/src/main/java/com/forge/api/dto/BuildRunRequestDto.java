package com.forge.api.dto;

public record BuildRunRequestDto(
        String repositoryId,
        String buildTool, // "maven" or "gradle"
        String tasks
) {}
