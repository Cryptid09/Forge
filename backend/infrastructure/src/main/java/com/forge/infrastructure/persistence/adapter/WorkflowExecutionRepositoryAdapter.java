package com.forge.infrastructure.persistence.adapter;

import com.forge.infrastructure.persistence.entity.WorkflowExecutionEntity;
import com.forge.infrastructure.persistence.entity.WorkflowStepExecutionEntity;
import com.forge.infrastructure.persistence.repository.JpaWorkflowExecutionRepository;
import com.forge.infrastructure.persistence.repository.JpaWorkflowStepExecutionRepository;
import com.forge.workflow.domain.WorkflowExecution;
import com.forge.workflow.domain.WorkflowStepExecution;
import com.forge.workflow.port.WorkflowExecutionRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class WorkflowExecutionRepositoryAdapter implements WorkflowExecutionRepository {

    private final JpaWorkflowExecutionRepository executionRepo;
    private final JpaWorkflowStepExecutionRepository stepExecutionRepo;

    public WorkflowExecutionRepositoryAdapter(JpaWorkflowExecutionRepository executionRepo,
                                              JpaWorkflowStepExecutionRepository stepExecutionRepo) {
        this.executionRepo = executionRepo;
        this.stepExecutionRepo = stepExecutionRepo;
    }

    @Override
    public WorkflowExecution save(WorkflowExecution execution) {
        return executionRepo.save(new WorkflowExecutionEntity(execution)).toDomain();
    }

    @Override
    public Optional<WorkflowExecution> findById(String id) {
        return executionRepo.findById(id).map(WorkflowExecutionEntity::toDomain);
    }

    @Override
    public List<WorkflowExecution> findAll() {
        return executionRepo.findAll().stream().map(WorkflowExecutionEntity::toDomain).toList();
    }

    @Override
    public List<WorkflowExecution> findByWorkflowId(String workflowId) {
        return executionRepo.findByWorkflowId(workflowId).stream()
                .map(WorkflowExecutionEntity::toDomain).toList();
    }

    @Override
    public WorkflowStepExecution saveStepExecution(WorkflowStepExecution stepExecution) {
        return stepExecutionRepo.save(new WorkflowStepExecutionEntity(stepExecution)).toDomain();
    }

    @Override
    public List<WorkflowStepExecution> findStepExecutionsByWorkflowExecutionId(String workflowExecutionId) {
        return stepExecutionRepo
                .findByWorkflowExecutionIdOrderByStepOrderAsc(workflowExecutionId)
                .stream().map(WorkflowStepExecutionEntity::toDomain).toList();
    }
}
