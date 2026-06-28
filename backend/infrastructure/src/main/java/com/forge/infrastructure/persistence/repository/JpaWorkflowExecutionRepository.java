package com.forge.infrastructure.persistence.repository;

import com.forge.infrastructure.persistence.entity.WorkflowExecutionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JpaWorkflowExecutionRepository extends JpaRepository<WorkflowExecutionEntity, String> {
    List<WorkflowExecutionEntity> findByWorkflowId(String workflowId);
}
