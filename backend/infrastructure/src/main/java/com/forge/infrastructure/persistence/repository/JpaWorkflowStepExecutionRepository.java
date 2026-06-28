package com.forge.infrastructure.persistence.repository;

import com.forge.infrastructure.persistence.entity.WorkflowStepExecutionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JpaWorkflowStepExecutionRepository extends JpaRepository<WorkflowStepExecutionEntity, String> {
    List<WorkflowStepExecutionEntity> findByWorkflowExecutionIdOrderByStepOrderAsc(String workflowExecutionId);
}
