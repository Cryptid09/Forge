package com.forge.devopstoolbox.infrastructure.tool;

import com.forge.devopstoolbox.core.port.ToolRegistry;
import com.forge.devopstoolbox.core.tool.Tool;
import com.forge.devopstoolbox.core.tool.ToolDescriptor;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class SpringToolRegistry implements ToolRegistry {

    private final Map<String, Tool> toolsById;

    public SpringToolRegistry(List<Tool> tools) {
        this.toolsById = tools.stream()
                .collect(Collectors.toUnmodifiableMap(
                        tool -> tool.descriptor().id(),
                        Function.identity(),
                        (left, right) -> {
                            throw new IllegalStateException(
                                    "Duplicate tool id: " + left.descriptor().id()
                            );
                        }
                ));
    }

    @Override
    public List<ToolDescriptor> listAll() {
        return toolsById.values().stream()
                .map(Tool::descriptor)
                .sorted((left, right) -> left.id().compareTo(right.id()))
                .toList();
    }

    @Override
    public Optional<Tool> getTool(String id) {
        return Optional.ofNullable(toolsById.get(id));
    }

    @Override
    public Optional<ToolDescriptor> getDescriptor(String id) {
        return getTool(id).map(Tool::descriptor);
    }
}
