package com.forge.devopstoolbox.infrastructure.persistence;

import com.forge.devopstoolbox.core.model.JobStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "jobs")
class JobEntity {

    @Id
    private UUID id;

    @Column(name = "tool_id", nullable = false)
    private String toolId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private JobStatus status;

    @Column(name = "started_at")
    private Instant startedAt;

    @Column(name = "finished_at")
    private Instant finishedAt;

    @Column(name = "duration_ms")
    private Long durationMs;

    @Column(columnDefinition = "TEXT")
    private String output;

    @Column(name = "error_message", columnDefinition = "TEXT")
    private String errorMessage;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    protected JobEntity() {
    }

    UUID getId() {
        return id;
    }

    String getToolId() {
        return toolId;
    }

    JobStatus getStatus() {
        return status;
    }

    Instant getStartedAt() {
        return startedAt;
    }

    Instant getFinishedAt() {
        return finishedAt;
    }

    Long getDurationMs() {
        return durationMs;
    }

    String getOutput() {
        return output;
    }

    String getErrorMessage() {
        return errorMessage;
    }

    Instant getCreatedAt() {
        return createdAt;
    }

    void setId(UUID id) {
        this.id = id;
    }

    void setToolId(String toolId) {
        this.toolId = toolId;
    }

    void setStatus(JobStatus status) {
        this.status = status;
    }

    void setStartedAt(Instant startedAt) {
        this.startedAt = startedAt;
    }

    void setFinishedAt(Instant finishedAt) {
        this.finishedAt = finishedAt;
    }

    void setDurationMs(Long durationMs) {
        this.durationMs = durationMs;
    }

    void setOutput(String output) {
        this.output = output;
    }

    void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}
