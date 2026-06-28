package com.forge.docker.tools;

import com.forge.core.tool.Tool;
import com.forge.core.tool.ToolContext;
import com.forge.core.tool.ToolDescriptor;
import com.forge.core.tool.ToolResult;
import com.forge.docker.service.DockerFacade;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

@Component
public class CreateContainerTool implements Tool {

    private final DockerFacade dockerFacade;

    private final ToolDescriptor descriptor = new ToolDescriptor(
            "docker-create-container",
            "Create Docker Container",
            "Creates a new Docker container from an image",
            "Docker",
            "1.0.0"
    );

    public CreateContainerTool(DockerFacade dockerFacade) {
        this.dockerFacade = dockerFacade;
    }

    @Override
    public ToolDescriptor getDescriptor() {
        return descriptor;
    }

    @Override
    public ToolResult execute(ToolContext context) {
        long start = System.currentTimeMillis();
        String name = context.parameters().get("name");
        String image = context.parameters().get("image");
        String envStr = context.parameters().get("env");
        String portsStr = context.parameters().get("ports");
        
        if (image == null || image.isEmpty()) {
            return new ToolResult(ToolResult.ExecutionStatus.FAILURE, null, "image parameter is required", Duration.ofMillis(System.currentTimeMillis() - start), 1);
        }

        List<String> env = envStr != null && !envStr.isEmpty() ? Arrays.asList(envStr.split(",")) : null;
        List<String> ports = portsStr != null && !portsStr.isEmpty() ? Arrays.asList(portsStr.split(",")) : null;

        try {
            String containerId = dockerFacade.createContainer(name, image, env, ports);
            return new ToolResult(ToolResult.ExecutionStatus.SUCCESS, "Container created with ID " + containerId, null, Duration.ofMillis(System.currentTimeMillis() - start), 0);
        } catch (Exception e) {
            return new ToolResult(ToolResult.ExecutionStatus.FAILURE, null, e.getMessage(), Duration.ofMillis(System.currentTimeMillis() - start), 1);
        }
    }
}
