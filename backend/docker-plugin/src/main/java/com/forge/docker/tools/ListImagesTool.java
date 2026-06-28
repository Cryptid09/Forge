package com.forge.docker.tools;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.dockerjava.api.model.Image;
import com.forge.core.tool.Tool;
import com.forge.core.tool.ToolContext;
import com.forge.core.tool.ToolDescriptor;
import com.forge.core.tool.ToolResult;
import com.forge.docker.service.DockerFacade;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.List;

@Component
public class ListImagesTool implements Tool {

    private final DockerFacade dockerFacade;
    private final ObjectMapper objectMapper = new ObjectMapper();

    private final ToolDescriptor descriptor = new ToolDescriptor(
            "docker-list-images",
            "List Docker Images",
            "Lists all Docker images on the host",
            "Docker",
            "1.0.0"
    );

    public ListImagesTool(DockerFacade dockerFacade) {
        this.dockerFacade = dockerFacade;
    }

    @Override
    public ToolDescriptor getDescriptor() {
        return descriptor;
    }

    @Override
    public ToolResult execute(ToolContext context) {
        long start = System.currentTimeMillis();
        try {
            List<Image> images = dockerFacade.listImages();
            String output = objectMapper.writeValueAsString(images);
            return new ToolResult(
                    ToolResult.ExecutionStatus.SUCCESS,
                    output,
                    null,
                    Duration.ofMillis(System.currentTimeMillis() - start),
                    0
            );
        } catch (Exception e) {
            return new ToolResult(
                    ToolResult.ExecutionStatus.FAILURE,
                    null,
                    e.getMessage(),
                    Duration.ofMillis(System.currentTimeMillis() - start),
                    1
            );
        }
    }
}
