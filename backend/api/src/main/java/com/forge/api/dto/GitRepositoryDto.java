package com.forge.api.dto;

import java.time.Instant;

public record GitRepositoryDto(
        String id,
        String name,
        String remoteUrl,
        String localPath,
        Instant createdAt
) {}
