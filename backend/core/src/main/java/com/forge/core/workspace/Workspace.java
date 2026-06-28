package com.forge.core.workspace;

import java.time.Instant;
import java.util.UUID;

public class Workspace {
    private UUID id;
    private String name;
    private String description;
    private String rootDirectory;
    private Instant createdAt;
    private Instant updatedAt;

    public Workspace() {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    public Workspace(String name, String description, String rootDirectory) {
        this();
        this.name = name;
        this.description = description;
        this.rootDirectory = rootDirectory;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public String getRootDirectory() { return rootDirectory; }
    public void setRootDirectory(String rootDirectory) { this.rootDirectory = rootDirectory; }
    
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    
    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
}
