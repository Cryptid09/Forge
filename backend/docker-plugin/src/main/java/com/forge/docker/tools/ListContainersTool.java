package com.forge.docker.tools;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.dockerjava.api.model.Container;
import com.forge.core.tool.Tool;
import com.forge.core.tool.ToolContext;
import com.forge.core.tool.ToolDescriptor;
import com.forge.core.tool.ToolResult;
import com.forge.docker.service.DockerFacade;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.List;

@Component
public class ListContainersTool implements Tool {

    private final DockerFacade dockerFacade;
    private final ObjectMapper objectMapper = new ObjectMapper();

    private final ToolDescriptor descriptor = new ToolDescriptor(
            "docker-list-containers",
            "List Docker Containers",
            "Lists all Docker containers on the host",
            "Docker",
            "1.0.0"
    );

    public ListContainersTool(DockerFacade dockerFacade) {
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
            List<Container> containers = dockerFacade.listContainers();
            String output = objectMapper.writeValueAsString(containers);
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
