package com.forge.api.controller;

import com.forge.api.dto.WorkflowExecutionDto;
import com.forge.api.dto.WorkflowStepExecutionDto;
import com.forge.workflow.domain.WorkflowExecution;
import com.forge.workflow.domain.WorkflowStepExecution;
import com.forge.workflow.service.WorkflowService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/workflow-executions")
@CrossOrigin("*")
public class WorkflowExecutionController {

    private final WorkflowService workflowService;

    public WorkflowExecutionController(WorkflowService workflowService) {
        this.workflowService = workflowService;
    }

    @GetMapping
    public List<WorkflowExecutionDto> listExecutions() {
        return workflowService.listExecutions().stream().map(this::toDto).toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<WorkflowExecutionDto> getExecution(@PathVariable String id) {
        try {
            return ResponseEntity.ok(toDto(workflowService.getExecution(id)));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    private WorkflowExecutionDto toDto(WorkflowExecution e) {
        List<WorkflowStepExecutionDto> stepDtos = e.getStepExecutions() == null ? List.of() :
                e.getStepExecutions().stream().map(this::toStepDto).toList();

        return new WorkflowExecutionDto(
                e.getId(), e.getWorkflowId(), e.getWorkflowName(),
                e.getStatus() != null ? e.getStatus().name() : null,
                e.getStartedAt(), e.getFinishedAt(),
                e.getDuration() != null ? e.getDuration().toMillis() : null,
                stepDtos
        );
    }

    private WorkflowStepExecutionDto toStepDto(WorkflowStepExecution s) {
        return new WorkflowStepExecutionDto(
                s.getId(), s.getWorkflowExecutionId(), s.getStepId(),
                s.getStepOrder(), s.getStepName(), s.getToolId(),
                s.getToolExecutionJobId(),
                s.getStatus() != null ? s.getStatus().name() : null,
                s.getStartedAt(), s.getFinishedAt(),
                s.getAttemptNumber(), s.getErrorMessage()
        );
    }
}
