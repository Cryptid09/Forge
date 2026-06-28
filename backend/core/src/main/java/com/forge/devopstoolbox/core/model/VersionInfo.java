package com.forge.devopstoolbox.core.model;

public record VersionInfo(
        String version,
        String artifact,
        String javaVersion,
        String springBootVersion
) {
}
