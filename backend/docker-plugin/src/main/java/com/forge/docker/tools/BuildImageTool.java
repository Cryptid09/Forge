package com.forge.docker.tools;

import com.forge.core.tool.Tool;
import com.forge.core.tool.ToolContext;
import com.forge.core.tool.ToolDescriptor;
import com.forge.core.tool.ToolResult;
import com.forge.docker.service.DockerFacade;
import org.springframework.stereotype.Component;

import java.io.File;
import java.time.Duration;

@Component
public class BuildImageTool implements Tool {

    private final DockerFacade dockerFacade;

    private final ToolDescriptor descriptor = new ToolDescriptor(
            "docker-build-image",
            "Build Docker Image",
            "Builds a Docker image from a local path",
            "Docker",
            "1.0.0"
    );

    public BuildImageTool(DockerFacade dockerFacade) {
        this.dockerFacade = dockerFacade;
    }

    @Override
    public ToolDescriptor getDescriptor() {
        return descriptor;
    }

    @Override
    public ToolResult execute(ToolContext context) {
        long start = System.currentTimeMillis();
        String contextDir = context.parameters().get("contextDir");
        String imageTag = context.parameters().get("imageTag");
        
        if (contextDir == null || contextDir.isEmpty() || imageTag == null || imageTag.isEmpty()) {
            return new ToolResult(ToolResult.ExecutionStatus.FAILURE, null, "contextDir and imageTag parameters are required", Duration.ofMillis(System.currentTimeMillis() - start), 1);
        }

        try {
            dockerFacade.buildImage(new File(contextDir), imageTag);
            return new ToolResult(ToolResult.ExecutionStatus.SUCCESS, "Image " + imageTag + " built successfully", null, Duration.ofMillis(System.currentTimeMillis() - start), 0);
        } catch (Exception e) {
            return new ToolResult(ToolResult.ExecutionStatus.FAILURE, null, e.getMessage(), Duration.ofMillis(System.currentTimeMillis() - start), 1);
        }
    }
}
