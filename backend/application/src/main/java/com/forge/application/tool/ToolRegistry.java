package com.forge.application.tool;

import com.forge.core.tool.Tool;
import com.forge.core.tool.ToolDescriptor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ToolRegistry {

    private final Map<String, Tool> toolMap;

    public ToolRegistry(List<Tool> tools) {
        this.toolMap = tools.stream()
                .collect(Collectors.toMap(t -> t.getDescriptor().id(), Function.identity()));
    }

    public List<ToolDescriptor> listTools() {
        return toolMap.values().stream()
                .map(Tool::getDescriptor)
                .toList();
    }

    public Optional<Tool> getToolById(String id) {
        return Optional.ofNullable(toolMap.get(id));
    }
}
