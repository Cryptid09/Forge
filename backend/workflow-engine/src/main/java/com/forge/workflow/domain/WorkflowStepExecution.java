package com.forge.workflow.domain;

import java.time.Instant;

public class WorkflowStepExecution {
    private String id;
    private String workflowExecutionId;
    private String stepId;
    private String toolExecutionJobId;
    private int stepOrder;
    private String stepName;
    private String toolId;
    private StepStatus status;
    private Instant startedAt;
    private Instant finishedAt;
    private int attemptNumber;
    private String errorMessage;

    public WorkflowStepExecution() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getWorkflowExecutionId() { return workflowExecutionId; }
    public void setWorkflowExecutionId(String workflowExecutionId) { this.workflowExecutionId = workflowExecutionId; }

    public String getStepId() { return stepId; }
    public void setStepId(String stepId) { this.stepId = stepId; }

    public String getToolExecutionJobId() { return toolExecutionJobId; }
    public void setToolExecutionJobId(String toolExecutionJobId) { this.toolExecutionJobId = toolExecutionJobId; }

    public int getStepOrder() { return stepOrder; }
    public void setStepOrder(int stepOrder) { this.stepOrder = stepOrder; }

    public String getStepName() { return stepName; }
    public void setStepName(String stepName) { this.stepName = stepName; }

    public String getToolId() { return toolId; }
    public void setToolId(String toolId) { this.toolId = toolId; }

    public StepStatus getStatus() { return status; }
    public void setStatus(StepStatus status) { this.status = status; }

    public Instant getStartedAt() { return startedAt; }
    public void setStartedAt(Instant startedAt) { this.startedAt = startedAt; }

    public Instant getFinishedAt() { return finishedAt; }
    public void setFinishedAt(Instant finishedAt) { this.finishedAt = finishedAt; }

    public int getAttemptNumber() { return attemptNumber; }
    public void setAttemptNumber(int attemptNumber) { this.attemptNumber = attemptNumber; }

    public String getErrorMessage() { return errorMessage; }
    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }
}
