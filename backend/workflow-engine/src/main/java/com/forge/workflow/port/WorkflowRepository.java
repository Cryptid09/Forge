package com.forge.workflow.port;

import com.forge.workflow.domain.Workflow;

import java.util.List;
import java.util.Optional;

public interface WorkflowRepository {
    Workflow save(Workflow workflow);
    Optional<Workflow> findById(String id);
    List<Workflow> findAll();
    void deleteById(String id);
}
