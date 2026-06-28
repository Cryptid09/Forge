package com.forge.workflow.service;

import com.forge.application.execution.ExecutionService;
import com.forge.core.event.Event;
import com.forge.core.event.EventBus;
import com.forge.core.job.Job;
import com.forge.core.job.JobStatus;
import com.forge.workflow.domain.*;
import com.forge.workflow.port.WorkflowExecutionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Core orchestrator. Executes workflow steps sequentially.
 *
 * <p>For each step the effective execution policy is resolved using three layers:
 * <pre>
 *   Platform Defaults → Workflow Defaults → Step Overrides
 * </pre>
 * This resolution is handled entirely by {@link ExecutionPolicy#merge} and
 * {@link WorkflowStep#resolveEffectivePolicy}, so the engine never needs to
 * change when new policy fields are added.
 */
@Service
public class WorkflowEngine {

    private static final Logger log = LoggerFactory.getLogger(WorkflowEngine.class);

    private final ExecutionService executionService;
    private final WorkflowExecutionRepository executionRepository;
    private final EventBus eventBus;

    public WorkflowEngine(ExecutionService executionService,
                          WorkflowExecutionRepository executionRepository,
                          EventBus eventBus) {
        this.executionService = executionService;
        this.executionRepository = executionRepository;
        this.eventBus = eventBus;
    }

    /** Runs the workflow in the calling thread — invoke from a background thread. */
    public WorkflowExecution execute(Workflow workflow) {
        Instant startedAt = Instant.now();

        WorkflowExecution workflowExecution = buildExecution(workflow, startedAt);
        executionRepository.save(workflowExecution);

        publishEvent("WorkflowStarted", null,
                "Workflow '" + workflow.getName() + "' [" + workflowExecution.getId() + "] started");

        List<WorkflowStep> orderedSteps = workflow.getSteps().stream()
                .sorted(Comparator.comparingInt(WorkflowStep::getStepOrder))
                .toList();

        boolean workflowFailed = false;

        for (WorkflowStep step : orderedSteps) {
            // Three-layer resolution: Platform → Workflow → Step
            ExecutionPolicy effectivePolicy = step.resolveEffectivePolicy(workflow.getExecutionPolicy());

            boolean continueOnFailure = Boolean.TRUE.equals(effectivePolicy.getContinueOnFailure());
            int maxAttempts = Math.max(1, safeInt(effectivePolicy.getRetryCount()) + 1);
            long retryDelayMs = safeLong(effectivePolicy.getRetryDelayMs());

            WorkflowStepExecution stepExecution = beginStepExecution(workflowExecution, step);
            publishEvent("WorkflowStepStarted", null,
                    "Step '" + step.getName() + "' (order=" + step.getStepOrder() +
                    ") starting [continueOnFailure=" + continueOnFailure +
                    ", maxAttempts=" + maxAttempts + "]");

            Job lastJob = null;
            boolean stepSucceeded = false;

            for (int attempt = 1; attempt <= maxAttempts; attempt++) {
                stepExecution.setAttemptNumber(attempt);
                executionRepository.saveStepExecution(stepExecution);

                try {
                    Map<String, String> params = step.getParameters() != null ? step.getParameters() : Map.of();
                    lastJob = executionService.executeToolSync(step.getToolId(), params);
                    stepExecution.setToolExecutionJobId(lastJob.getId());

                    if (lastJob.getStatus() == JobStatus.COMPLETED) {
                        stepSucceeded = true;
                        break;
                    }
                } catch (Exception e) {
                    log.warn("Step '{}' attempt {} threw exception: {}", step.getName(), attempt, e.getMessage());
                    stepExecution.setErrorMessage(e.getMessage());
                }

                if (attempt < maxAttempts) {
                    log.info("Step '{}' failed on attempt {}. Retrying after {}ms...",
                            step.getName(), attempt, retryDelayMs);
                    try {
                        Thread.sleep(retryDelayMs);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                    }
                }
            }

            stepExecution.setFinishedAt(Instant.now());

            if (stepSucceeded) {
                stepExecution.setStatus(StepStatus.COMPLETED);
                executionRepository.saveStepExecution(stepExecution);
                publishEvent("WorkflowStepCompleted",
                        lastJob != null ? lastJob.getId() : null,
                        "Step '" + step.getName() + "' completed successfully");
            } else {
                stepExecution.setStatus(StepStatus.FAILED);
                if (lastJob != null) stepExecution.setToolExecutionJobId(lastJob.getId());
                executionRepository.saveStepExecution(stepExecution);
                publishEvent("WorkflowStepFailed",
                        lastJob != null ? lastJob.getId() : null,
                        "Step '" + step.getName() + "' failed" +
                        (continueOnFailure ? " — continuing (continueOnFailure=true)" : " — halting workflow"));

                if (!continueOnFailure) {
                    workflowFailed = true;
                    break;
                }
            }
        }

        Instant finishedAt = Instant.now();
        workflowExecution.setFinishedAt(finishedAt);
        workflowExecution.setDuration(Duration.between(startedAt, finishedAt));

        if (workflowFailed) {
            workflowExecution.setStatus(WorkflowStatus.FAILED);
            executionRepository.save(workflowExecution);
            publishEvent("WorkflowFailed", null, "Workflow '" + workflow.getName() + "' failed");
        } else {
            workflowExecution.setStatus(WorkflowStatus.COMPLETED);
            executionRepository.save(workflowExecution);
            publishEvent("WorkflowCompleted", null,
                    "Workflow '" + workflow.getName() + "' completed successfully");
        }

        return workflowExecution;
    }

    // ── Helpers ──────────────────────────────────────────────────────────────

    private WorkflowExecution buildExecution(Workflow workflow, Instant startedAt) {
        WorkflowExecution exec = new WorkflowExecution();
        exec.setId(UUID.randomUUID().toString());
        exec.setWorkflowId(workflow.getId());
        exec.setWorkflowName(workflow.getName());
        exec.setStatus(WorkflowStatus.RUNNING);
        exec.setStartedAt(startedAt);
        exec.setStepExecutions(new ArrayList<>());
        return exec;
    }

    private WorkflowStepExecution beginStepExecution(WorkflowExecution exec, WorkflowStep step) {
        WorkflowStepExecution se = new WorkflowStepExecution();
        se.setId(UUID.randomUUID().toString());
        se.setWorkflowExecutionId(exec.getId());
        se.setStepId(step.getId());
        se.setStepOrder(step.getStepOrder());
        se.setStepName(step.getName());
        se.setToolId(step.getToolId());
        se.setStatus(StepStatus.RUNNING);
        se.setStartedAt(Instant.now());
        se.setAttemptNumber(1);
        exec.getStepExecutions().add(se);
        return executionRepository.saveStepExecution(se);
    }

    private void publishEvent(String type, String jobId, String payload) {
        eventBus.publish(new Event(type, jobId, payload));
    }

    private int safeInt(Integer v)  { return v != null ? v : 0; }
    private long safeLong(Long v)   { return v != null ? v : 0L; }
}
