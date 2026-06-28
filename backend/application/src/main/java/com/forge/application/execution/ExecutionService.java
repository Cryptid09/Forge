package com.forge.application.execution;

import com.forge.application.job.JobManager;
import com.forge.application.tool.ToolRegistry;
import com.forge.core.event.Event;
import com.forge.core.event.EventBus;
import com.forge.core.job.Job;
import com.forge.core.job.JobStatus;
import com.forge.core.tool.Tool;
import com.forge.core.tool.ToolContext;
import com.forge.core.tool.ToolResult;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Service
public class ExecutionService {

    private final ToolRegistry toolRegistry;
    private final JobManager jobManager;
    private final EventBus eventBus;

    public ExecutionService(ToolRegistry toolRegistry, JobManager jobManager, EventBus eventBus) {
        this.toolRegistry = toolRegistry;
        this.jobManager = jobManager;
        this.eventBus = eventBus;
    }

    public Job executeToolAsync(String toolId, Map<String, String> parameters) {
        Tool tool = toolRegistry.getToolById(toolId)
                .orElseThrow(() -> new IllegalArgumentException("Tool not found: " + toolId));

        Job job = jobManager.createJob(toolId);
        
        CompletableFuture.runAsync(() -> {
            try {
                jobManager.updateStatus(job.getId(), JobStatus.RUNNING);
                eventBus.publish(new Event("ToolStarted", job.getId(), "Started execution of tool " + toolId));

                ToolContext context = new ToolContext(parameters, "/tmp", Map.of(), Instant.now(), "system");
                ToolResult result = tool.execute(context);

                if (result.executionStatus() == ToolResult.ExecutionStatus.SUCCESS) {
                    jobManager.finishJob(job.getId(), result.outputMessage(), result.executionDuration());
                    eventBus.publish(new Event("ToolCompleted", job.getId(), "Completed execution with success"));
                } else {
                    jobManager.failJob(job.getId(), result.errorMessage(), result.executionDuration());
                    eventBus.publish(new Event("ToolFailed", job.getId(), "Failed with error: " + result.errorMessage()));
                }
            } catch (Exception e) {
                jobManager.failJob(job.getId(), e.getMessage(), null);
                eventBus.publish(new Event("ToolFailed", job.getId(), "Exception: " + e.getMessage()));
            }
        });

        return job;
    }

    /**
     * Synchronously executes a tool and returns the completed/failed Job.
     * The calling thread blocks until the tool finishes.
     * Used by the WorkflowEngine to sequence step execution.
     */
    public Job executeToolSync(String toolId, Map<String, String> parameters) {
        Tool tool = toolRegistry.getToolById(toolId)
                .orElseThrow(() -> new IllegalArgumentException("Tool not found: " + toolId));

        Job job = jobManager.createJob(toolId);
        jobManager.updateStatus(job.getId(), JobStatus.RUNNING);
        eventBus.publish(new Event("ToolStarted", job.getId(), "Started execution of tool " + toolId));

        ToolContext context = new ToolContext(parameters, "/tmp", Map.of(), Instant.now(), "system");
        ToolResult result = tool.execute(context);

        if (result.executionStatus() == ToolResult.ExecutionStatus.SUCCESS) {
            jobManager.finishJob(job.getId(), result.outputMessage(), result.executionDuration());
            eventBus.publish(new Event("ToolCompleted", job.getId(), "Completed execution with success"));
        } else {
            jobManager.failJob(job.getId(), result.errorMessage(), result.executionDuration());
            eventBus.publish(new Event("ToolFailed", job.getId(), "Failed with error: " + result.errorMessage()));
        }
        return jobManager.getJobById(job.getId());
    }
}
