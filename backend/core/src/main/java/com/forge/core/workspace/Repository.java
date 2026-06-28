package com.forge.core.workspace;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Repository {
    private UUID id;
    private UUID workspaceId;
    private String name;
    private String path;
    private Map<String, Boolean> capabilities;
    private Instant createdAt;
    private Instant updatedAt;

    public Repository() {
        this.id = UUID.randomUUID();
        this.capabilities = new HashMap<>();
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    public Repository(UUID workspaceId, String name, String path) {
        this();
        this.workspaceId = workspaceId;
        this.name = name;
        this.path = path;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    
    public UUID getWorkspaceId() { return workspaceId; }
    public void setWorkspaceId(UUID workspaceId) { this.workspaceId = workspaceId; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getPath() { return path; }
    public void setPath(String path) { this.path = path; }
    
    public Map<String, Boolean> getCapabilities() { return capabilities; }
    public void setCapabilities(Map<String, Boolean> capabilities) { this.capabilities = capabilities; }
    
    public void addCapability(String capability, boolean isSupported) {
        this.capabilities.put(capability, isSupported);
    }
    
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    
    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
}
