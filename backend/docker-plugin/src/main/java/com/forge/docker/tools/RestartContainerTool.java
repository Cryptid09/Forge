package com.forge.docker.tools;

import com.forge.core.tool.Tool;
import com.forge.core.tool.ToolContext;
import com.forge.core.tool.ToolDescriptor;
import com.forge.core.tool.ToolResult;
import com.forge.docker.service.DockerFacade;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
public class RestartContainerTool implements Tool {

    private final DockerFacade dockerFacade;

    private final ToolDescriptor descriptor = new ToolDescriptor(
            "docker-restart-container",
            "Restart Docker Container",
            "Restarts a running Docker container",
            "Docker",
            "1.0.0"
    );

    public RestartContainerTool(DockerFacade dockerFacade) {
        this.dockerFacade = dockerFacade;
    }

    @Override
    public ToolDescriptor getDescriptor() {
        return descriptor;
    }

    @Override
    public ToolResult execute(ToolContext context) {
        long start = System.currentTimeMillis();
        String containerId = context.parameters().get("containerId");
        
        if (containerId == null || containerId.isEmpty()) {
            return new ToolResult(ToolResult.ExecutionStatus.FAILURE, null, "containerId parameter is required", Duration.ofMillis(System.currentTimeMillis() - start), 1);
        }

        try {
            dockerFacade.restartContainer(containerId);
            return new ToolResult(ToolResult.ExecutionStatus.SUCCESS, "Container " + containerId + " restarted successfully", null, Duration.ofMillis(System.currentTimeMillis() - start), 0);
        } catch (Exception e) {
            return new ToolResult(ToolResult.ExecutionStatus.FAILURE, null, e.getMessage(), Duration.ofMillis(System.currentTimeMillis() - start), 1);
        }
    }
}
