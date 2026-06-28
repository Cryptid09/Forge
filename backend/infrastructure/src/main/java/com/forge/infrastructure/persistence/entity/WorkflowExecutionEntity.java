package com.forge.infrastructure.persistence.entity;

import com.forge.workflow.domain.WorkflowExecution;
import com.forge.workflow.domain.WorkflowStatus;
import jakarta.persistence.*;

import java.time.Duration;
import java.time.Instant;

@Entity
@Table(name = "workflow_executions")
public class WorkflowExecutionEntity {

    @Id
    private String id;
    private String workflowId;
    private String workflowName;
    private String status;
    private Instant startedAt;
    private Instant finishedAt;
    private Long durationMs;

    public WorkflowExecutionEntity() {}

    public WorkflowExecutionEntity(WorkflowExecution e) {
        this.id = e.getId();
        this.workflowId = e.getWorkflowId();
        this.workflowName = e.getWorkflowName();
        this.status = e.getStatus() != null ? e.getStatus().name() : WorkflowStatus.PENDING.name();
        this.startedAt = e.getStartedAt();
        this.finishedAt = e.getFinishedAt();
        this.durationMs = e.getDuration() != null ? e.getDuration().toMillis() : null;
    }

    public WorkflowExecution toDomain() {
        WorkflowExecution e = new WorkflowExecution();
        e.setId(id);
        e.setWorkflowId(workflowId);
        e.setWorkflowName(workflowName);
        e.setStatus(WorkflowStatus.valueOf(status));
        e.setStartedAt(startedAt);
        e.setFinishedAt(finishedAt);
        e.setDuration(durationMs != null ? Duration.ofMillis(durationMs) : null);
        return e;
    }
}
