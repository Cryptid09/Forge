package com.forge.devopstoolbox.api.dto;

public record VersionResponse(
        String version,
        String artifact,
        String javaVersion,
        String springBootVersion
) {
}
