package com.forge.infrastructure.persistence.repository;

import com.forge.infrastructure.persistence.entity.WorkflowEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaWorkflowRepository extends JpaRepository<WorkflowEntity, String> {
}
