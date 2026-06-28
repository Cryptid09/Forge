package com.forge.core.job;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

public class Job {
    private String id;
    private String toolId;
    private JobStatus status;
    private Instant startedAt;
    private Instant finishedAt;
    private Duration duration;
    private String output;
    private String errorMessage;

    public Job() {}

    public Job(String toolId) {
        this.id = UUID.randomUUID().toString();
        this.toolId = toolId;
        this.status = JobStatus.QUEUED;
        this.startedAt = Instant.now();
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getToolId() { return toolId; }
    public void setToolId(String toolId) { this.toolId = toolId; }
    
    public JobStatus getStatus() { return status; }
    public void setStatus(JobStatus status) { this.status = status; }
    
    public Instant getStartedAt() { return startedAt; }
    public void setStartedAt(Instant startedAt) { this.startedAt = startedAt; }
    
    public Instant getFinishedAt() { return finishedAt; }
    public void setFinishedAt(Instant finishedAt) { this.finishedAt = finishedAt; }
    
    public Duration getDuration() { return duration; }
    public void setDuration(Duration duration) { this.duration = duration; }
    
    public String getOutput() { return output; }
    public void setOutput(String output) { this.output = output; }
    
    public String getErrorMessage() { return errorMessage; }
    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }
}
