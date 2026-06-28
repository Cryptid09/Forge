package com.forge.devopstoolbox.application.service;

import com.forge.devopstoolbox.core.model.Job;
import com.forge.devopstoolbox.core.port.ToolRegistry;
import com.forge.devopstoolbox.core.tool.Tool;
import com.forge.devopstoolbox.core.tool.ToolContext;
import com.forge.devopstoolbox.core.tool.ToolResult;
import java.util.Map;
import java.util.concurrent.Executor;

public class ExecutionService {

    private final ToolRegistry toolRegistry;
    private final JobManager jobManager;
    private final Executor taskExecutor;

    public ExecutionService(ToolRegistry toolRegistry, JobManager jobManager, Executor taskExecutor) {
        this.toolRegistry = toolRegistry;
        this.jobManager = jobManager;
        this.taskExecutor = taskExecutor;
    }

    public Job execute(String toolId, Map<String, String> parameters) {
        Tool tool = toolRegistry.getTool(toolId)
                .orElseThrow(() -> new ToolNotFoundException("Tool not found: " + toolId));

        Job job = jobManager.createJob(toolId);

        taskExecutor.execute(() -> runExecution(tool, job, parameters));

        return job;
    }

    private void runExecution(Tool tool, Job job, Map<String, String> parameters) {
        jobManager.markRunning(job.id());
        long startNanos = System.nanoTime();

        try {
            ToolContext context = ToolContext.create(job.id(), parameters);
            ToolResult result = tool.execute(context);
            long durationMs = result.durationMs() > 0
                    ? result.durationMs()
                    : (System.nanoTime() - startNanos) / 1_000_000;

            if (result.status() == ToolResult.ExecutionStatus.SUCCESS) {
                ToolResult successResult = new ToolResult(
                        ToolResult.ExecutionStatus.SUCCESS,
                        result.outputMessage(),
                        null,
                        durationMs,
                        result.exitCode()
                );
                jobManager.completeJob(job.id(), successResult);
            } else {
                jobManager.failJob(
                        job.id(),
                        result.errorMessage() != null ? result.errorMessage() : "Tool execution failed",
                        durationMs
                );
            }
        } catch (Exception exception) {
            long durationMs = (System.nanoTime() - startNanos) / 1_000_000;
            String message = exception.getMessage() != null ? exception.getMessage() : exception.getClass().getSimpleName();
            jobManager.failJob(job.id(), message, durationMs);
        }
    }
}
