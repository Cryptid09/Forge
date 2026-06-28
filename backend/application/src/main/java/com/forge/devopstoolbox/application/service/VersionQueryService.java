package com.forge.devopstoolbox.application.service;

import com.forge.devopstoolbox.core.model.VersionInfo;

public class VersionQueryService {

    private final String version;
    private final String artifact;
    private final String javaVersion;
    private final String springBootVersion;

    public VersionQueryService(
            String version,
            String artifact,
            String javaVersion,
            String springBootVersion
    ) {
        this.version = version;
        this.artifact = artifact;
        this.javaVersion = javaVersion;
        this.springBootVersion = springBootVersion;
    }

    public VersionInfo getVersion() {
        return new VersionInfo(version, artifact, javaVersion, springBootVersion);
    }
}
