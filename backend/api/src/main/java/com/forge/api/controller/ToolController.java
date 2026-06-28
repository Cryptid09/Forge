package com.forge.api.controller;

import com.forge.api.dto.ExecuteToolRequest;
import com.forge.application.execution.ExecutionService;
import com.forge.application.tool.ToolRegistry;
import com.forge.core.job.Job;
import com.forge.core.tool.Tool;
import com.forge.core.tool.ToolDescriptor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tools")
@CrossOrigin("*")
public class ToolController {

    private final ToolRegistry toolRegistry;
    private final ExecutionService executionService;

    public ToolController(ToolRegistry toolRegistry, ExecutionService executionService) {
        this.toolRegistry = toolRegistry;
        this.executionService = executionService;
    }

    @GetMapping
    public List<ToolDescriptor> listTools() {
        return toolRegistry.listTools();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ToolDescriptor> getTool(@PathVariable String id) {
        return toolRegistry.getToolById(id)
                .map(Tool::getDescriptor)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{id}/execute")
    public ResponseEntity<Job> executeTool(
            @PathVariable String id,
            @RequestBody(required = false) ExecuteToolRequest request) {
        
        var parameters = request != null && request.parameters() != null 
                ? request.parameters() 
                : java.util.Map.<String, String>of();
                
        try {
            Job job = executionService.executeToolAsync(id, parameters);
            return ResponseEntity.accepted().body(job);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
