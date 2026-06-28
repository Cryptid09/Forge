package com.forge.infrastructure.persistence.entity;

import com.forge.core.job.Job;
import com.forge.core.job.JobStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Duration;
import java.time.Instant;

@Entity
@Table(name = "jobs")
public class JobEntity {

    @Id
    private String id;
    private String toolId;
    private String status;
    private Instant startedAt;
    private Instant finishedAt;
    private Long durationMs;
    private String output;
    private String errorMessage;

    public JobEntity() {}

    public JobEntity(Job job) {
        this.id = job.getId();
        this.toolId = job.getToolId();
        this.status = job.getStatus().name();
        this.startedAt = job.getStartedAt();
        this.finishedAt = job.getFinishedAt();
        this.durationMs = job.getDuration() != null ? job.getDuration().toMillis() : null;
        this.output = job.getOutput();
        this.errorMessage = job.getErrorMessage();
    }

    public Job toDomain() {
        Job job = new Job();
        job.setId(id);
        job.setToolId(toolId);
        job.setStatus(JobStatus.valueOf(status));
        job.setStartedAt(startedAt);
        job.setFinishedAt(finishedAt);
        job.setDuration(durationMs != null ? Duration.ofMillis(durationMs) : null);
        job.setOutput(output);
        job.setErrorMessage(errorMessage);
        return job;
    }
}
