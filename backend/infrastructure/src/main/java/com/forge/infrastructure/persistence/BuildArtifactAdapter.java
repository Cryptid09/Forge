package com.forge.infrastructure.persistence;

import com.forge.build.domain.BuildArtifact;
import com.forge.build.port.BuildArtifactStore;
import com.forge.infrastructure.persistence.entity.BuildArtifactEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class BuildArtifactAdapter implements BuildArtifactStore {

    private final JpaBuildArtifactRepository jpaRepository;

    public BuildArtifactAdapter(JpaBuildArtifactRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public BuildArtifact save(BuildArtifact artifact) {
        BuildArtifactEntity entity = new BuildArtifactEntity();
        entity.setId(artifact.getId());
        entity.setRepositoryId(artifact.getRepositoryId());
        entity.setName(artifact.getName());
        entity.setType(artifact.getType());
        entity.setSizeBytes(artifact.getSizeBytes());
        entity.setLocalPath(artifact.getLocalPath());
        entity.setBuildTime(artifact.getBuildTime());
        jpaRepository.save(entity);
        return artifact;
    }

    @Override
    public List<BuildArtifact> findByRepositoryId(String repositoryId) {
        return jpaRepository.findByRepositoryId(repositoryId).stream().map(this::toDomain).collect(Collectors.toList());
    }

    @Override
    public List<BuildArtifact> findAll() {
        return jpaRepository.findAll().stream().map(this::toDomain).collect(Collectors.toList());
    }

    @Override
    public void deleteById(String id) {
        jpaRepository.deleteById(id);
    }

    private BuildArtifact toDomain(BuildArtifactEntity entity) {
        BuildArtifact artifact = new BuildArtifact();
        artifact.setId(entity.getId());
        artifact.setRepositoryId(entity.getRepositoryId());
        artifact.setName(entity.getName());
        artifact.setType(entity.getType());
        artifact.setSizeBytes(entity.getSizeBytes());
        artifact.setLocalPath(entity.getLocalPath());
        artifact.setBuildTime(entity.getBuildTime());
        return artifact;
    }
}
