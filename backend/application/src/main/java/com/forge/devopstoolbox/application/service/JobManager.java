package com.forge.devopstoolbox.application.service;

import com.forge.devopstoolbox.core.model.EventType;
import com.forge.devopstoolbox.core.model.Job;
import com.forge.devopstoolbox.core.model.JobStatus;
import com.forge.devopstoolbox.core.model.PlatformEvent;
import com.forge.devopstoolbox.core.port.EventBus;
import com.forge.devopstoolbox.core.port.JobRepository;
import com.forge.devopstoolbox.core.tool.ToolResult;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class JobManager {

    private final JobRepository jobRepository;
    private final EventBus eventBus;

    public JobManager(JobRepository jobRepository, EventBus eventBus) {
        this.jobRepository = jobRepository;
        this.eventBus = eventBus;
    }

    public Job createJob(String toolId) {
        Instant now = Instant.now();
        Job job = Job.queued(UUID.randomUUID(), toolId, now);
        Job saved = jobRepository.save(job);
        publishEvent(EventType.JOB_CREATED, saved, "Job created");
        return saved;
    }

    public Job markRunning(UUID jobId) {
        Job job = requireJob(jobId);
        Instant now = Instant.now();
        Job updated = new Job(
                job.id(),
                job.toolId(),
                JobStatus.RUNNING,
                now,
                null,
                null,
                job.output(),
                job.errorMessage(),
                job.createdAt()
        );
        Job saved = jobRepository.save(updated);
        publishEvent(EventType.JOB_RUNNING, saved, "Job running");
        publishEvent(EventType.TOOL_STARTED, saved, "Tool started");
        return saved;
    }

    public Job completeJob(UUID jobId, ToolResult result) {
        Job job = requireJob(jobId);
        Instant now = Instant.now();
        long durationMs = result.durationMs();
        Job updated = new Job(
                job.id(),
                job.toolId(),
                JobStatus.COMPLETED,
                job.startedAt() != null ? job.startedAt() : now,
                now,
                durationMs,
                result.outputMessage(),
                null,
                job.createdAt()
        );
        Job saved = jobRepository.save(updated);
        publishEvent(EventType.TOOL_COMPLETED, saved, result.outputMessage());
        publishEvent(EventType.JOB_COMPLETED, saved, "Job completed");
        return saved;
    }

    public Job failJob(UUID jobId, String errorMessage, long durationMs) {
        Job job = requireJob(jobId);
        Instant now = Instant.now();
        Job updated = new Job(
                job.id(),
                job.toolId(),
                JobStatus.FAILED,
                job.startedAt() != null ? job.startedAt() : now,
                now,
                durationMs,
                job.output(),
                errorMessage,
                job.createdAt()
        );
        Job saved = jobRepository.save(updated);
        publishEvent(EventType.TOOL_FAILED, saved, errorMessage);
        publishEvent(EventType.JOB_FAILED, saved, errorMessage);
        return saved;
    }

    public Optional<Job> getJob(UUID jobId) {
        return jobRepository.findById(jobId);
    }

    public List<Job> getAllJobs() {
        return jobRepository.findAll();
    }

    private Job requireJob(UUID jobId) {
        return jobRepository.findById(jobId)
                .orElseThrow(() -> new IllegalStateException("Job not found: " + jobId));
    }

    private void publishEvent(EventType eventType, Job job, String payload) {
        PlatformEvent event = new PlatformEvent(
                UUID.randomUUID(),
                eventType,
                job.id(),
                job.toolId(),
                Instant.now(),
                payload
        );
        eventBus.publish(event);
    }
}
