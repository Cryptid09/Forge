package com.forge.api.dto;

import java.util.Map;

public record WorkflowStepDto(
        String id,
        int stepOrder,
        String name,
        String toolId,
        Map<String, String> parameters,
        /** Per-step execution policy overrides. Null keys inherit from workflow / platform defaults. */
        Map<String, Object> executionPolicy
) {}
