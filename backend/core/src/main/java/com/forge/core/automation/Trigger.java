package com.forge.core.automation;

import java.time.Instant;
import java.util.UUID;

public class Trigger {
    private UUID id;
    private UUID workspaceId;
    private UUID workflowId;
    private String triggerType;
    private boolean enabled;
    private String configuration;
    private Instant createdAt;
    private Instant updatedAt;

    public Trigger() {
        this.id = UUID.randomUUID();
        this.enabled = true;
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    public Trigger(UUID workspaceId, UUID workflowId, String triggerType, String configuration) {
        this();
        this.workspaceId = workspaceId;
        this.workflowId = workflowId;
        this.triggerType = triggerType;
        this.configuration = configuration;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    
    public UUID getWorkspaceId() { return workspaceId; }
    public void setWorkspaceId(UUID workspaceId) { this.workspaceId = workspaceId; }
    
    public UUID getWorkflowId() { return workflowId; }
    public void setWorkflowId(UUID workflowId) { this.workflowId = workflowId; }
    
    public String getTriggerType() { return triggerType; }
    public void setTriggerType(String triggerType) { this.triggerType = triggerType; }
    
    public boolean isEnabled() { return enabled; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }
    
    public String getConfiguration() { return configuration; }
    public void setConfiguration(String configuration) { this.configuration = configuration; }
    
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    
    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
}
