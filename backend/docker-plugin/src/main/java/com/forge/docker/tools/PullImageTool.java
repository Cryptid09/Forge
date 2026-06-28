package com.forge.docker.tools;

import com.forge.core.tool.Tool;
import com.forge.core.tool.ToolContext;
import com.forge.core.tool.ToolDescriptor;
import com.forge.core.tool.ToolResult;
import com.forge.docker.service.DockerFacade;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
public class PullImageTool implements Tool {

    private final DockerFacade dockerFacade;

    private final ToolDescriptor descriptor = new ToolDescriptor(
            "docker-pull-image",
            "Pull Docker Image",
            "Pulls a Docker image from a registry",
            "Docker",
            "1.0.0"
    );

    public PullImageTool(DockerFacade dockerFacade) {
        this.dockerFacade = dockerFacade;
    }

    @Override
    public ToolDescriptor getDescriptor() {
        return descriptor;
    }

    @Override
    public ToolResult execute(ToolContext context) {
        long start = System.currentTimeMillis();
        String image = context.parameters().get("image");
        
        if (image == null || image.isEmpty()) {
            return new ToolResult(ToolResult.ExecutionStatus.FAILURE, null, "image parameter is required", Duration.ofMillis(System.currentTimeMillis() - start), 1);
        }

        try {
            dockerFacade.pullImage(image);
            return new ToolResult(ToolResult.ExecutionStatus.SUCCESS, "Image " + image + " pulled successfully", null, Duration.ofMillis(System.currentTimeMillis() - start), 0);
        } catch (Exception e) {
            return new ToolResult(ToolResult.ExecutionStatus.FAILURE, null, e.getMessage(), Duration.ofMillis(System.currentTimeMillis() - start), 1);
        }
    }
}
