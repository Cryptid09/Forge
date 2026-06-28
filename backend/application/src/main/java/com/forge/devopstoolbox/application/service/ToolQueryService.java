package com.forge.devopstoolbox.application.service;

import com.forge.devopstoolbox.core.model.Job;
import com.forge.devopstoolbox.core.model.JobStatistics;
import com.forge.devopstoolbox.core.port.JobRepository;
import com.forge.devopstoolbox.core.tool.ToolDescriptor;
import com.forge.devopstoolbox.core.port.ToolRegistry;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class ToolQueryService {

    private final ToolRegistry toolRegistry;

    public ToolQueryService(ToolRegistry toolRegistry) {
        this.toolRegistry = toolRegistry;
    }

    public List<ToolDescriptor> listTools() {
        return toolRegistry.listAll();
    }

    public Optional<ToolDescriptor> getTool(String id) {
        return toolRegistry.getDescriptor(id);
    }
}
