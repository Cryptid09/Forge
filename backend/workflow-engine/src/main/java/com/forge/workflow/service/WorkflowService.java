package com.forge.workflow.service;

import com.forge.workflow.domain.Workflow;
import com.forge.workflow.domain.WorkflowExecution;
import com.forge.workflow.domain.WorkflowStep;
import com.forge.workflow.port.WorkflowExecutionRepository;
import com.forge.workflow.port.WorkflowRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
public class WorkflowService {

    private final WorkflowRepository workflowRepository;
    private final WorkflowExecutionRepository executionRepository;
    private final WorkflowValidator validator;
    private final WorkflowEngine engine;

    public WorkflowService(WorkflowRepository workflowRepository,
                           WorkflowExecutionRepository executionRepository,
                           WorkflowValidator validator,
                           WorkflowEngine engine) {
        this.workflowRepository = workflowRepository;
        this.executionRepository = executionRepository;
        this.validator = validator;
        this.engine = engine;
    }

    public List<Workflow> listWorkflows() {
        return workflowRepository.findAll();
    }

    public Workflow getWorkflow(String id) {
        return workflowRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Workflow not found: " + id));
    }

    public Workflow createWorkflow(Workflow workflow) {
        validator.validate(workflow);
        workflow.setId(UUID.randomUUID().toString());
        workflow.setCreatedAt(Instant.now());
        workflow.setUpdatedAt(Instant.now());
        workflow.setVersion(1);
        workflow.setEnabled(true);

        // Assign IDs to steps and link to workflow
        if (workflow.getSteps() != null) {
            for (WorkflowStep step : workflow.getSteps()) {
                step.setId(UUID.randomUUID().toString());
                step.setWorkflowId(workflow.getId());
            }
        }
        return workflowRepository.save(workflow);
    }

    public Workflow updateWorkflow(String id, Workflow updated) {
        Workflow existing = getWorkflow(id);
        updated.setId(existing.getId());
        updated.setCreatedAt(existing.getCreatedAt());
        updated.setUpdatedAt(Instant.now());
        updated.setVersion(existing.getVersion() + 1);
        validator.validate(updated);

        if (updated.getSteps() != null) {
            for (WorkflowStep step : updated.getSteps()) {
                if (step.getId() == null) step.setId(UUID.randomUUID().toString());
                step.setWorkflowId(id);
            }
        }
        return workflowRepository.save(updated);
    }

    public void deleteWorkflow(String id) {
        getWorkflow(id); // throws if not found
        workflowRepository.deleteById(id);
    }

    public WorkflowExecution triggerExecution(String workflowId) {
        Workflow workflow = getWorkflow(workflowId);
        validator.validate(workflow);

        // Run the engine asynchronously so the REST call returns immediately
        CompletableFuture.runAsync(() -> engine.execute(workflow));

        // Return a placeholder execution record so the caller gets an ID to track
        WorkflowExecution placeholder = new WorkflowExecution();
        placeholder.setId(UUID.randomUUID().toString());
        placeholder.setWorkflowId(workflowId);
        placeholder.setWorkflowName(workflow.getName());
        placeholder.setStartedAt(Instant.now());
        com.forge.workflow.domain.WorkflowStatus pending = com.forge.workflow.domain.WorkflowStatus.PENDING;
        placeholder.setStatus(pending);
        return executionRepository.save(placeholder);
    }

    public List<WorkflowExecution> listExecutions() {
        return executionRepository.findAll();
    }

    public WorkflowExecution getExecution(String id) {
        WorkflowExecution execution = executionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("WorkflowExecution not found: " + id));
        execution.setStepExecutions(executionRepository.findStepExecutionsByWorkflowExecutionId(id));
        return execution;
    }
}
