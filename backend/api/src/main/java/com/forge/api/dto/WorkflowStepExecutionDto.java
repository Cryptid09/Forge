package com.forge.api.dto;

import java.time.Instant;

public record WorkflowStepExecutionDto(
        String id,
        String workflowExecutionId,
        String stepId,
        int stepOrder,
        String stepName,
        String toolId,
        String toolExecutionJobId,
        String status,
        Instant startedAt,
        Instant finishedAt,
        int attemptNumber,
        String errorMessage
) {}
