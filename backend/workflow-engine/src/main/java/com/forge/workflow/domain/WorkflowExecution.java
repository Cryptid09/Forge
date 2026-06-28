package com.forge.workflow.domain;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

public class WorkflowExecution {
    private String id;
    private String workflowId;
    private String workflowName;
    private WorkflowStatus status;
    private Instant startedAt;
    private Instant finishedAt;
    private Duration duration;
    private List<WorkflowStepExecution> stepExecutions;

    public WorkflowExecution() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getWorkflowId() { return workflowId; }
    public void setWorkflowId(String workflowId) { this.workflowId = workflowId; }

    public String getWorkflowName() { return workflowName; }
    public void setWorkflowName(String workflowName) { this.workflowName = workflowName; }

    public WorkflowStatus getStatus() { return status; }
    public void setStatus(WorkflowStatus status) { this.status = status; }

    public Instant getStartedAt() { return startedAt; }
    public void setStartedAt(Instant startedAt) { this.startedAt = startedAt; }

    public Instant getFinishedAt() { return finishedAt; }
    public void setFinishedAt(Instant finishedAt) { this.finishedAt = finishedAt; }

    public Duration getDuration() { return duration; }
    public void setDuration(Duration duration) { this.duration = duration; }

    public List<WorkflowStepExecution> getStepExecutions() { return stepExecutions; }
    public void setStepExecutions(List<WorkflowStepExecution> stepExecutions) { this.stepExecutions = stepExecutions; }
}
