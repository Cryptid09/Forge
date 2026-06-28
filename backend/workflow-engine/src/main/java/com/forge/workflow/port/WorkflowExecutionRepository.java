package com.forge.workflow.port;

import com.forge.workflow.domain.WorkflowExecution;
import com.forge.workflow.domain.WorkflowStepExecution;

import java.util.List;
import java.util.Optional;

public interface WorkflowExecutionRepository {
    WorkflowExecution save(WorkflowExecution execution);
    Optional<WorkflowExecution> findById(String id);
    List<WorkflowExecution> findAll();
    List<WorkflowExecution> findByWorkflowId(String workflowId);

    WorkflowStepExecution saveStepExecution(WorkflowStepExecution stepExecution);
    List<WorkflowStepExecution> findStepExecutionsByWorkflowExecutionId(String workflowExecutionId);
}
