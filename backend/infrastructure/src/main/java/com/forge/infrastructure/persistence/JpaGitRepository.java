package com.forge.infrastructure.persistence;

import com.forge.infrastructure.persistence.entity.GitRepositoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaGitRepository extends JpaRepository<GitRepositoryEntity, String> {
}
