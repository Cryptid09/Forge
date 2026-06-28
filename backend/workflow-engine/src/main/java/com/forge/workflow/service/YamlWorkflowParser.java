package com.forge.workflow.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.forge.workflow.domain.ExecutionPolicy;
import com.forge.workflow.domain.Workflow;
import com.forge.workflow.domain.WorkflowStep;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Parses a YAML workflow definition from either an inline String or a filesystem path.
 *
 * YAML format:
 * <pre>
 * name: My Workflow
 * description: Optional description
 * defaultContinueOnFailure: false
 * steps:
 *   - name: Step 1
 *     tool: echo-tool
 *     parameters:
 *       message: Hello
 *     retryCount: 2
 *     retryDelayMs: 1000
 *     continueOnFailure: true     # optional; overrides workflow default
 * </pre>
 */
@Component
public class YamlWorkflowParser {

    private final ObjectMapper yamlMapper = new ObjectMapper(new YAMLFactory());

    /** Parse from an inline YAML string (REST body). */
    public Workflow parseYaml(String yamlContent) {
        try {
            Map<?, ?> raw = yamlMapper.readValue(yamlContent, Map.class);
            return mapToWorkflow(raw);
        } catch (IOException e) {
            throw new IllegalArgumentException("Invalid YAML workflow definition: " + e.getMessage(), e);
        }
    }

    /** Parse from a filesystem path. */
    public Workflow parseYamlFile(String filePath) {
        try {
            Map<?, ?> raw = yamlMapper.readValue(new File(filePath), Map.class);
            return mapToWorkflow(raw);
        } catch (IOException e) {
            throw new IllegalArgumentException("Cannot read YAML file at path '" + filePath + "': " + e.getMessage(), e);
        }
    }

    @SuppressWarnings("unchecked")
    private Workflow mapToWorkflow(Map<?, ?> raw) {
        Workflow workflow = new Workflow();
        workflow.setName(getString(raw, "name", "Unnamed Workflow"));
        workflow.setDescription(getString(raw, "description", ""));
        workflow.setEnabled(true);
        workflow.setVersion(1);

        // Support both a nested executionPolicy map and legacy flat keys
        Object policyObj = raw.get("executionPolicy");
        ExecutionPolicy workflowPolicy = new ExecutionPolicy();
        if (policyObj instanceof Map<?, ?> pMap) {
            workflowPolicy = ExecutionPolicy.fromMap((Map<String, Object>) pMap);
        } else {
            // Legacy flat keys for backward compatibility
            Object cof = raw.get("defaultContinueOnFailure");
            if (cof != null) workflowPolicy.setContinueOnFailure(Boolean.parseBoolean(cof.toString()));
            Object rc = raw.get("retryCount");
            if (rc != null) workflowPolicy.setRetryCount(Integer.parseInt(rc.toString()));
            Object rd = raw.get("retryDelayMs");
            if (rd != null) workflowPolicy.setRetryDelayMs(Long.parseLong(rd.toString()));
        }
        workflow.setExecutionPolicy(workflowPolicy);

        List<WorkflowStep> steps = new ArrayList<>();
        Object stepsObj = raw.get("steps");
        if (stepsObj instanceof List<?> stepList) {
            int order = 1;
            for (Object stepObj : stepList) {
                if (stepObj instanceof Map<?, ?> stepMap) {
                    steps.add(mapToStep((Map<Object, Object>) stepMap, order++));
                }
            }
        }
        workflow.setSteps(steps);
        return workflow;
    }

    @SuppressWarnings("unchecked")
    private WorkflowStep mapToStep(Map<Object, Object> raw, int order) {
        WorkflowStep step = new WorkflowStep();
        step.setStepOrder(order);
        step.setName(getString(raw, "name", "Step " + order));
        step.setToolId(getString(raw, "tool", null));

        // Support nested executionPolicy map AND legacy flat keys on the step
        ExecutionPolicy stepPolicy = new ExecutionPolicy();
        Object policyObj = raw.get("executionPolicy");
        if (policyObj instanceof Map<?, ?> pMap) {
            stepPolicy = ExecutionPolicy.fromMap((Map<String, Object>) pMap);
        } else {
            Object cof = raw.get("continueOnFailure");
            if (cof != null) stepPolicy.setContinueOnFailure(Boolean.parseBoolean(cof.toString()));
            Object rc = raw.get("retryCount");
            if (rc != null) stepPolicy.setRetryCount(Integer.parseInt(rc.toString()));
            Object rd = raw.get("retryDelayMs");
            if (rd != null) stepPolicy.setRetryDelayMs(Long.parseLong(rd.toString()));
            Object to = raw.get("timeoutMs");
            if (to != null) stepPolicy.setTimeoutMs(Long.parseLong(to.toString()));
        }
        step.setExecutionPolicy(stepPolicy);

        Object params = raw.get("parameters");
        if (params instanceof Map<?, ?> paramsMap) {
            Map<String, String> parameters = new java.util.LinkedHashMap<>();
            paramsMap.forEach((k, v) -> parameters.put(k.toString(), v != null ? v.toString() : ""));
            step.setParameters(parameters);
        } else {
            step.setParameters(new java.util.LinkedHashMap<>());
        }
        return step;
    }

    private String getString(Map<?, ?> map, String key, String defaultValue) {
        Object val = map.get(key);
        return val != null ? val.toString() : defaultValue;
    }

    private int getInt(Map<?, ?> map, String key, int defaultValue) {
        Object val = map.get(key);
        if (val == null) return defaultValue;
        try { return Integer.parseInt(val.toString()); } catch (NumberFormatException e) { return defaultValue; }
    }

    private long getLong(Map<?, ?> map, String key, long defaultValue) {
        Object val = map.get(key);
        if (val == null) return defaultValue;
        try { return Long.parseLong(val.toString()); } catch (NumberFormatException e) { return defaultValue; }
    }
}
