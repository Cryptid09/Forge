package com.forge.infrastructure.persistence.adapter;

import com.forge.infrastructure.persistence.entity.WorkflowEntity;
import com.forge.infrastructure.persistence.repository.JpaWorkflowRepository;
import com.forge.workflow.domain.Workflow;
import com.forge.workflow.port.WorkflowRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class WorkflowRepositoryAdapter implements WorkflowRepository {

    private final JpaWorkflowRepository jpaRepository;

    public WorkflowRepositoryAdapter(JpaWorkflowRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Workflow save(Workflow workflow) {
        return jpaRepository.save(new WorkflowEntity(workflow)).toDomain();
    }

    @Override
    public Optional<Workflow> findById(String id) {
        return jpaRepository.findById(id).map(WorkflowEntity::toDomain);
    }

    @Override
    public List<Workflow> findAll() {
        return jpaRepository.findAll().stream().map(WorkflowEntity::toDomain).toList();
    }

    @Override
    public void deleteById(String id) {
        jpaRepository.deleteById(id);
    }
}
