package com.forge.api.dto;

import java.time.Instant;
import java.util.List;
import java.util.Map;

public record WorkflowDto(
        String id,
        String name,
        String description,
        int version,
        boolean enabled,
        /** Workflow-level execution policy defaults (Platform → Workflow → Step resolution). */
        Map<String, Object> executionPolicy,
        Instant createdAt,
        Instant updatedAt,
        List<WorkflowStepDto> steps
) {}
