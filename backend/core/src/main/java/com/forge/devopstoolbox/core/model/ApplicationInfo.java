package com.forge.devopstoolbox.core.model;

public record ApplicationInfo(
        String name,
        String description,
        String environment,
        String buildTime
) {
}
