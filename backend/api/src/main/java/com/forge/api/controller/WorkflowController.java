package com.forge.api.controller;

import com.forge.api.dto.*;
import com.forge.workflow.domain.*;
import com.forge.workflow.service.WorkflowService;
import com.forge.workflow.service.YamlWorkflowParser;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/workflows")
@CrossOrigin("*")
public class WorkflowController {

    private final WorkflowService workflowService;
    private final YamlWorkflowParser yamlParser;

    public WorkflowController(WorkflowService workflowService, YamlWorkflowParser yamlParser) {
        this.workflowService = workflowService;
        this.yamlParser = yamlParser;
    }

    @GetMapping
    public List<WorkflowDto> listWorkflows() {
        return workflowService.listWorkflows().stream().map(this::toDto).toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<WorkflowDto> getWorkflow(@PathVariable String id) {
        try {
            return ResponseEntity.ok(toDto(workflowService.getWorkflow(id)));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<?> createWorkflow(@RequestBody Map<String, Object> body) {
        try {
            Workflow workflow = fromBody(body);
            return ResponseEntity.ok(toDto(workflowService.createWorkflow(workflow)));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    /** Import a workflow from inline YAML string. Body: { "yaml": "..." } */
    @PostMapping("/import/yaml")
    public ResponseEntity<?> importYaml(@RequestBody Map<String, String> body) {
        try {
            String yaml = body.get("yaml");
            if (yaml == null || yaml.isBlank()) {
                return ResponseEntity.badRequest().body(Map.of("error", "yaml field is required"));
            }
            Workflow workflow = yamlParser.parseYaml(yaml);
            return ResponseEntity.ok(toDto(workflowService.createWorkflow(workflow)));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    /** Import a workflow from a server-side filesystem path. Body: { "path": "/absolute/path/to/workflow.yml" } */
    @PostMapping("/import/file")
    public ResponseEntity<?> importFile(@RequestBody Map<String, String> body) {
        try {
            String path = body.get("path");
            if (path == null || path.isBlank()) {
                return ResponseEntity.badRequest().body(Map.of("error", "path field is required"));
            }
            Workflow workflow = yamlParser.parseYamlFile(path);
            return ResponseEntity.ok(toDto(workflowService.createWorkflow(workflow)));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateWorkflow(@PathVariable String id, @RequestBody Map<String, Object> body) {
        try {
            Workflow workflow = fromBody(body);
            return ResponseEntity.ok(toDto(workflowService.updateWorkflow(id, workflow)));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWorkflow(@PathVariable String id) {
        try {
            workflowService.deleteWorkflow(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{id}/execute")
    public ResponseEntity<?> executeWorkflow(@PathVariable String id) {
        try {
            WorkflowExecution execution = workflowService.triggerExecution(id);
            return ResponseEntity.accepted().body(toExecutionDto(execution));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // ── Mapping helpers ──────────────────────────────────────────────────────

    @SuppressWarnings("unchecked")
    private Workflow fromBody(Map<String, Object> body) {
        Workflow workflow = new Workflow();
        workflow.setName(str(body, "name"));
        workflow.setDescription(str(body, "description"));

        // Workflow-level execution policy
        Object policyObj = body.get("executionPolicy");
        if (policyObj instanceof Map<?, ?> pMap) {
            workflow.setExecutionPolicy(ExecutionPolicy.fromMap((Map<String, Object>) pMap));
        }

        Object stepsObj = body.get("steps");
        if (stepsObj instanceof List<?> rawSteps) {
            List<WorkflowStep> steps = rawSteps.stream()
                    .filter(s -> s instanceof Map)
                    .map(s -> stepFromMap((Map<String, Object>) s))
                    .toList();
            workflow.setSteps(steps);
        }
        return workflow;
    }

    @SuppressWarnings("unchecked")
    private WorkflowStep stepFromMap(Map<String, Object> m) {
        WorkflowStep step = new WorkflowStep();
        step.setName(str(m, "name"));
        step.setToolId(str(m, "toolId"));
        step.setStepOrder(m.get("stepOrder") instanceof Number n ? n.intValue() : 0);

        // Per-step execution policy overrides
        Object policyObj = m.get("executionPolicy");
        if (policyObj instanceof Map<?, ?> pMap) {
            step.setExecutionPolicy(ExecutionPolicy.fromMap((Map<String, Object>) pMap));
        }

        Object params = m.get("parameters");
        if (params instanceof Map<?, ?> pm) {
            step.setParameters(pm.entrySet().stream()
                    .collect(java.util.stream.Collectors.toMap(
                            e -> e.getKey().toString(),
                            e -> e.getValue() != null ? e.getValue().toString() : "")));
        } else {
            step.setParameters(Map.of());
        }
        return step;
    }

    private String str(Map<?, ?> map, String key) {
        Object v = map.get(key);
        return v != null ? v.toString() : null;
    }

    private WorkflowDto toDto(Workflow w) {
        List<WorkflowStepDto> stepDtos = w.getSteps() == null ? List.of() :
                w.getSteps().stream().map(s -> new WorkflowStepDto(
                        s.getId(), s.getStepOrder(), s.getName(), s.getToolId(),
                        s.getParameters(),
                        s.getExecutionPolicy() != null ? s.getExecutionPolicy().toMap() : Map.of()
                )).toList();
        return new WorkflowDto(w.getId(), w.getName(), w.getDescription(),
                w.getVersion(), w.isEnabled(),
                w.getExecutionPolicy() != null ? w.getExecutionPolicy().toMap() : Map.of(),
                w.getCreatedAt(), w.getUpdatedAt(), stepDtos);
    }

    private WorkflowExecutionDto toExecutionDto(WorkflowExecution e) {
        return new WorkflowExecutionDto(
                e.getId(), e.getWorkflowId(), e.getWorkflowName(),
                e.getStatus() != null ? e.getStatus().name() : null,
                e.getStartedAt(), e.getFinishedAt(),
                e.getDuration() != null ? e.getDuration().toMillis() : null,
                List.of()
        );
    }
}
