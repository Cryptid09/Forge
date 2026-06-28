package com.forge.api.dto;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

public record WorkflowExecutionDto(
        String id,
        String workflowId,
        String workflowName,
        String status,
        Instant startedAt,
        Instant finishedAt,
        Long durationMs,
        List<WorkflowStepExecutionDto> stepExecutions
) {}
