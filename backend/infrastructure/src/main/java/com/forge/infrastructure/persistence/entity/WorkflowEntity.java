package com.forge.infrastructure.persistence.entity;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.forge.workflow.domain.ExecutionPolicy;
import com.forge.workflow.domain.Workflow;
import jakarta.persistence.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "workflows")
public class WorkflowEntity {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Id
    private String id;
    private String name;
    @Column(columnDefinition = "TEXT")
    private String description;
    private int version;
    private boolean enabled;
    private Instant createdAt;
    private Instant updatedAt;
    @Column(name = "execution_policy_json", columnDefinition = "TEXT")
    private String executionPolicyJson;

    @OneToMany(mappedBy = "workflowId", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @OrderBy("stepOrder ASC")
    private List<WorkflowStepEntity> steps = new ArrayList<>();

    public WorkflowEntity() {}

    public WorkflowEntity(Workflow w) {
        this.id = w.getId();
        this.name = w.getName();
        this.description = w.getDescription();
        this.version = w.getVersion();
        this.enabled = w.isEnabled();
        this.createdAt = w.getCreatedAt();
        this.updatedAt = w.getUpdatedAt();
        this.executionPolicyJson = serializePolicy(w.getExecutionPolicy());
        if (w.getSteps() != null) {
            this.steps = w.getSteps().stream().map(WorkflowStepEntity::new).toList();
        }
    }

    public Workflow toDomain() {
        Workflow w = new Workflow();
        w.setId(id);
        w.setName(name);
        w.setDescription(description);
        w.setVersion(version);
        w.setEnabled(enabled);
        w.setCreatedAt(createdAt);
        w.setUpdatedAt(updatedAt);
        w.setExecutionPolicy(deserializePolicy(executionPolicyJson));
        w.setSteps(steps.stream().map(WorkflowStepEntity::toDomain).toList());
        return w;
    }

    static String serializePolicy(ExecutionPolicy policy) {
        if (policy == null) return null;
        try { return MAPPER.writeValueAsString(policy.toMap()); } catch (Exception e) { return null; }
    }

    static ExecutionPolicy deserializePolicy(String json) {
        if (json == null || json.isBlank()) return new ExecutionPolicy();
        try {
            Map<String, Object> map = MAPPER.readValue(json, new TypeReference<>() {});
            return ExecutionPolicy.fromMap(map);
        } catch (Exception e) { return new ExecutionPolicy(); }
    }
}
