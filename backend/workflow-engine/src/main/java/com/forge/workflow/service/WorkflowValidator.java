package com.forge.workflow.service;

import com.forge.application.tool.ToolRegistry;
import com.forge.workflow.domain.ExecutionPolicy;
import com.forge.workflow.domain.Workflow;
import com.forge.workflow.domain.WorkflowStep;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class WorkflowValidator {

    private final ToolRegistry toolRegistry;

    public WorkflowValidator(ToolRegistry toolRegistry) {
        this.toolRegistry = toolRegistry;
    }

    /**
     * Validates a workflow before saving or executing.
     * @throws IllegalArgumentException on first validation failure.
     */
    public void validate(Workflow workflow) {
        List<String> errors = new ArrayList<>();

        if (workflow.getName() == null || workflow.getName().isBlank()) {
            errors.add("Workflow name is required");
        }
        if (workflow.getSteps() == null || workflow.getSteps().isEmpty()) {
            errors.add("Workflow must contain at least one step");
        } else {
            Set<Integer> orders = new HashSet<>();
            for (WorkflowStep step : workflow.getSteps()) {
                if (step.getToolId() == null || step.getToolId().isBlank()) {
                    errors.add("Step '" + step.getName() + "' is missing a tool ID");
                } else if (toolRegistry.getToolById(step.getToolId()).isEmpty()) {
                    errors.add("Step '" + step.getName() + "' references unknown tool: " + step.getToolId());
                }
                if (!orders.add(step.getStepOrder())) {
                    errors.add("Duplicate step order: " + step.getStepOrder());
                }
                // Validate the effective policy at the step level
                ExecutionPolicy policy = step.getExecutionPolicy();
                if (policy != null) {
                    Integer rc = policy.getRetryCount();
                    Long rd = policy.getRetryDelayMs();
                    if (rc != null && rc < 0) errors.add("Step '" + step.getName() + "' has invalid retryCount: " + rc);
                    if (rd != null && rd < 0) errors.add("Step '" + step.getName() + "' has invalid retryDelayMs: " + rd);
                }
            }
        }

        if (!errors.isEmpty()) {
            throw new IllegalArgumentException("Workflow validation failed:\n" + String.join("\n", errors));
        }
    }
}
