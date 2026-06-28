package com.forge.workflow.domain;

import java.time.Instant;
import java.util.List;

public class Workflow {
    private String id;
    private String name;
    private String description;
    private int version;
    private boolean enabled;
    private Instant createdAt;
    private Instant updatedAt;
    private List<WorkflowStep> steps;

    /**
     * Workflow-level execution policy defaults.
     * Applied as the middle layer in: Platform Defaults → Workflow Defaults → Step Overrides.
     * Any field left null here will fall back to {@link PlatformExecutionPolicy#DEFAULTS}.
     */
    private ExecutionPolicy executionPolicy = new ExecutionPolicy();

    public Workflow() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public int getVersion() { return version; }
    public void setVersion(int version) { this.version = version; }

    public boolean isEnabled() { return enabled; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }

    public List<WorkflowStep> getSteps() { return steps; }
    public void setSteps(List<WorkflowStep> steps) { this.steps = steps; }

    public ExecutionPolicy getExecutionPolicy() { return executionPolicy; }
    public void setExecutionPolicy(ExecutionPolicy executionPolicy) {
        this.executionPolicy = executionPolicy != null ? executionPolicy : new ExecutionPolicy();
    }

    // ── Convenience accessors (delegates to merged: platform → workflow) ──────

    /** Returns the effective continue-on-failure default for this workflow's steps. */
    public boolean resolveDefaultContinueOnFailure() {
        ExecutionPolicy merged = ExecutionPolicy.merge(PlatformExecutionPolicy.DEFAULTS, executionPolicy);
        return Boolean.TRUE.equals(merged.getContinueOnFailure());
    }
}
