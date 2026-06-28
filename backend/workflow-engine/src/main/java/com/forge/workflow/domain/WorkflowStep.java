package com.forge.workflow.domain;

import java.util.Map;

public class WorkflowStep {
    private String id;
    private String workflowId;
    private int stepOrder;
    private String name;
    private String toolId;
    private Map<String, String> parameters;

    /**
     * Step-level execution policy overrides.
     * Any field set here takes precedence over the workflow-level policy.
     * Any field left null falls through to workflow defaults, then platform defaults.
     *
     * Resolution order: Platform Defaults → Workflow Defaults → Step Overrides
     */
    private ExecutionPolicy executionPolicy = new ExecutionPolicy();

    public WorkflowStep() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getWorkflowId() { return workflowId; }
    public void setWorkflowId(String workflowId) { this.workflowId = workflowId; }

    public int getStepOrder() { return stepOrder; }
    public void setStepOrder(int stepOrder) { this.stepOrder = stepOrder; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getToolId() { return toolId; }
    public void setToolId(String toolId) { this.toolId = toolId; }

    public Map<String, String> getParameters() { return parameters; }
    public void setParameters(Map<String, String> parameters) { this.parameters = parameters; }

    public ExecutionPolicy getExecutionPolicy() { return executionPolicy; }
    public void setExecutionPolicy(ExecutionPolicy executionPolicy) {
        this.executionPolicy = executionPolicy != null ? executionPolicy : new ExecutionPolicy();
    }

    /**
     * Resolves the fully effective execution policy for this step using the three-layer hierarchy.
     * @param workflowPolicy the parent workflow's execution policy
     */
    public ExecutionPolicy resolveEffectivePolicy(ExecutionPolicy workflowPolicy) {
        return ExecutionPolicy.merge(
                PlatformExecutionPolicy.DEFAULTS,  // lowest priority
                workflowPolicy,                    // workflow-level defaults
                this.executionPolicy               // step-level overrides (highest priority)
        );
    }
}
