package com.forge.devopstoolbox.api.controller;

import com.forge.devopstoolbox.api.dto.ExecuteToolRequest;
import com.forge.devopstoolbox.api.dto.JobResponse;
import com.forge.devopstoolbox.api.dto.ToolDescriptorResponse;
import com.forge.devopstoolbox.api.exception.ResourceNotFoundException;
import com.forge.devopstoolbox.api.mapper.PlatformMapper;
import com.forge.devopstoolbox.application.service.ExecutionService;
import com.forge.devopstoolbox.application.service.ToolNotFoundException;
import com.forge.devopstoolbox.application.service.ToolQueryService;
import com.forge.devopstoolbox.core.model.Job;
import com.forge.devopstoolbox.core.tool.ToolDescriptor;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/tools")
class ToolController {

    private final ToolQueryService toolQueryService;
    private final ExecutionService executionService;

    ToolController(ToolQueryService toolQueryService, ExecutionService executionService) {
        this.toolQueryService = toolQueryService;
        this.executionService = executionService;
    }

    @GetMapping
    List<ToolDescriptorResponse> listTools() {
        return toolQueryService.listTools().stream()
                .map(PlatformMapper::toToolResponse)
                .toList();
    }

    @GetMapping("/{id}")
    ToolDescriptorResponse getTool(@PathVariable String id) {
        ToolDescriptor descriptor = toolQueryService.getTool(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tool not found: " + id));
        return PlatformMapper.toToolResponse(descriptor);
    }

    @PostMapping("/{id}/execute")
    @ResponseStatus(HttpStatus.ACCEPTED)
    JobResponse executeTool(@PathVariable String id, @Valid @RequestBody ExecuteToolRequest request) {
        try {
            Job job = executionService.execute(id, request.parameters());
            return PlatformMapper.toJobResponse(job);
        } catch (ToolNotFoundException exception) {
            throw new ResourceNotFoundException(exception.getMessage());
        }
    }
}
