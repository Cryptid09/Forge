package com.forge.devopstoolbox.core.port;

import com.forge.devopstoolbox.core.tool.Tool;
import com.forge.devopstoolbox.core.tool.ToolDescriptor;
import java.util.List;
import java.util.Optional;

public interface ToolRegistry {

    List<ToolDescriptor> listAll();

    Optional<Tool> getTool(String id);

    Optional<ToolDescriptor> getDescriptor(String id);
}
