package com.forge.docker.tools;

import com.forge.core.tool.Tool;
import com.forge.core.tool.ToolContext;
import com.forge.core.tool.ToolDescriptor;
import com.forge.core.tool.ToolResult;
import com.forge.docker.service.DockerFacade;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
public class RemoveImageTool implements Tool {

    private final DockerFacade dockerFacade;

    private final ToolDescriptor descriptor = new ToolDescriptor(
            "docker-remove-image",
            "Remove Docker Image",
            "Removes a Docker image",
            "Docker",
            "1.0.0"
    );

    public RemoveImageTool(DockerFacade dockerFacade) {
        this.dockerFacade = dockerFacade;
    }

    @Override
    public ToolDescriptor getDescriptor() {
        return descriptor;
    }

    @Override
    public ToolResult execute(ToolContext context) {
        long start = System.currentTimeMillis();
        String imageId = context.parameters().get("imageId");
        
        if (imageId == null || imageId.isEmpty()) {
            return new ToolResult(ToolResult.ExecutionStatus.FAILURE, null, "imageId parameter is required", Duration.ofMillis(System.currentTimeMillis() - start), 1);
        }

        try {
            dockerFacade.removeImage(imageId);
            return new ToolResult(ToolResult.ExecutionStatus.SUCCESS, "Image " + imageId + " removed successfully", null, Duration.ofMillis(System.currentTimeMillis() - start), 0);
        } catch (Exception e) {
            return new ToolResult(ToolResult.ExecutionStatus.FAILURE, null, e.getMessage(), Duration.ofMillis(System.currentTimeMillis() - start), 1);
        }
    }
}
