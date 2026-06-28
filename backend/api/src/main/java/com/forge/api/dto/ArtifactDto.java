package com.forge.api.dto;

import java.time.Instant;

public record ArtifactDto(
        String id,
        String repositoryId,
        String name,
        String type,
        Long sizeBytes,
        String localPath,
        Instant buildTime
) {}
