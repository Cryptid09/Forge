package com.forge.infrastructure.persistence.entity;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.forge.workflow.domain.ExecutionPolicy;
import com.forge.workflow.domain.WorkflowStep;
import jakarta.persistence.*;

import java.util.Map;

@Entity
@Table(name = "workflow_steps")
public class WorkflowStepEntity {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Id
    private String id;
    private String workflowId;
    private int stepOrder;
    private String name;
    private String toolId;
    @Column(columnDefinition = "TEXT")
    private String parameters; // stored as JSON
    @Column(name = "execution_policy_json", columnDefinition = "TEXT")
    private String executionPolicyJson; // stored as JSON

    public WorkflowStepEntity() {}

    public WorkflowStepEntity(WorkflowStep s) {
        this.id = s.getId();
        this.workflowId = s.getWorkflowId();
        this.stepOrder = s.getStepOrder();
        this.name = s.getName();
        this.toolId = s.getToolId();
        this.executionPolicyJson = WorkflowEntity.serializePolicy(s.getExecutionPolicy());
        try {
            this.parameters = MAPPER.writeValueAsString(s.getParameters());
        } catch (Exception e) {
            this.parameters = "{}";
        }
    }

    public WorkflowStep toDomain() {
        WorkflowStep s = new WorkflowStep();
        s.setId(id);
        s.setWorkflowId(workflowId);
        s.setStepOrder(stepOrder);
        s.setName(name);
        s.setToolId(toolId);
        s.setExecutionPolicy(WorkflowEntity.deserializePolicy(executionPolicyJson));
        try {
            Map<String, String> params = MAPPER.readValue(
                    parameters != null ? parameters : "{}", new TypeReference<>() {});
            s.setParameters(params);
        } catch (Exception e) {
            s.setParameters(Map.of());
        }
        return s;
    }
}
