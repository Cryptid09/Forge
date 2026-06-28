package com.forge.infrastructure.persistence.entity;

import com.forge.workflow.domain.StepStatus;
import com.forge.workflow.domain.WorkflowStepExecution;
import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "workflow_step_executions")
public class WorkflowStepExecutionEntity {

    @Id
    private String id;
    private String workflowExecutionId;
    private String stepId;
    private int stepOrder;
    private String stepName;
    private String toolId;
    private String toolExecutionJobId;
    private String status;
    private Instant startedAt;
    private Instant finishedAt;
    private int attemptNumber;
    @Column(columnDefinition = "TEXT")
    private String errorMessage;

    public WorkflowStepExecutionEntity() {}

    public WorkflowStepExecutionEntity(WorkflowStepExecution s) {
        this.id = s.getId();
        this.workflowExecutionId = s.getWorkflowExecutionId();
        this.stepId = s.getStepId();
        this.stepOrder = s.getStepOrder();
        this.stepName = s.getStepName();
        this.toolId = s.getToolId();
        this.toolExecutionJobId = s.getToolExecutionJobId();
        this.status = s.getStatus() != null ? s.getStatus().name() : StepStatus.PENDING.name();
        this.startedAt = s.getStartedAt();
        this.finishedAt = s.getFinishedAt();
        this.attemptNumber = s.getAttemptNumber();
        this.errorMessage = s.getErrorMessage();
    }

    public WorkflowStepExecution toDomain() {
        WorkflowStepExecution s = new WorkflowStepExecution();
        s.setId(id);
        s.setWorkflowExecutionId(workflowExecutionId);
        s.setStepId(stepId);
        s.setStepOrder(stepOrder);
        s.setStepName(stepName);
        s.setToolId(toolId);
        s.setToolExecutionJobId(toolExecutionJobId);
        s.setStatus(StepStatus.valueOf(status));
        s.setStartedAt(startedAt);
        s.setFinishedAt(finishedAt);
        s.setAttemptNumber(attemptNumber);
        s.setErrorMessage(errorMessage);
        return s;
    }
}
