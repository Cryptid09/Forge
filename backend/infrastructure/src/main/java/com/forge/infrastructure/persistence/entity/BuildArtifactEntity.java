package com.forge.infrastructure.persistence.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;

@Entity
@Table(name = "build_artifacts")
public class BuildArtifactEntity {

    @Id
    private String id;
    private String repositoryId;
    private String name;
    private String type;
    private Long sizeBytes;
    private String localPath;
    private Instant buildTime;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getRepositoryId() { return repositoryId; }
    public void setRepositoryId(String repositoryId) { this.repositoryId = repositoryId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public Long getSizeBytes() { return sizeBytes; }
    public void setSizeBytes(Long sizeBytes) { this.sizeBytes = sizeBytes; }
    public String getLocalPath() { return localPath; }
    public void setLocalPath(String localPath) { this.localPath = localPath; }
    public Instant getBuildTime() { return buildTime; }
    public void setBuildTime(Instant buildTime) { this.buildTime = buildTime; }
}
