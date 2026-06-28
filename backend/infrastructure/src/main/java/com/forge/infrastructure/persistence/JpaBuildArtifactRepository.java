package com.forge.infrastructure.persistence;

import com.forge.infrastructure.persistence.entity.BuildArtifactEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JpaBuildArtifactRepository extends JpaRepository<BuildArtifactEntity, String> {
    List<BuildArtifactEntity> findByRepositoryId(String repositoryId);
}
