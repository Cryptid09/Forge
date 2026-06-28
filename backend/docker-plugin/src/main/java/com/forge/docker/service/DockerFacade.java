package com.forge.docker.service;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.model.Container;
import com.github.dockerjava.api.model.Image;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DockerFacade {

    private final DockerClient dockerClient;

    public DockerFacade(DockerClient dockerClient) {
        this.dockerClient = dockerClient;
    }

    public List<Container> listContainers() {
        return dockerClient.listContainersCmd().withShowAll(true).exec();
    }

    public void startContainer(String containerId) {
        dockerClient.startContainerCmd(containerId).exec();
    }

    public void stopContainer(String containerId) {
        dockerClient.stopContainerCmd(containerId).exec();
    }

    public void restartContainer(String containerId) {
        dockerClient.restartContainerCmd(containerId).exec();
    }

    public void removeContainer(String containerId) {
        dockerClient.removeContainerCmd(containerId).withForce(true).exec();
    }

    public List<Image> listImages() {
        return dockerClient.listImagesCmd().exec();
    }

    public void pullImage(String image) {
        try {
            dockerClient.pullImageCmd(image).start().awaitCompletion();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Image pull interrupted", e);
        }
    }
    
    public void removeImage(String imageId) {
        dockerClient.removeImageCmd(imageId).withForce(true).exec();
    }

    public void buildImage(java.io.File baseDir, String imageTag) {
        try {
            dockerClient.buildImageCmd(baseDir)
                    .withTags(java.util.Collections.singleton(imageTag))
                    .start().awaitCompletion();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Image build interrupted", e);
        }
    }

    public String createContainer(String name, String image, List<String> env, List<String> ports) {
        // Basic mapping for ports: "8080:80" -> PortBinding
        List<com.github.dockerjava.api.model.PortBinding> portBindings = new java.util.ArrayList<>();
        if (ports != null) {
            for (String port : ports) {
                portBindings.add(com.github.dockerjava.api.model.PortBinding.parse(port));
            }
        }
        
        CreateContainerResponse response = dockerClient.createContainerCmd(image)
                .withName(name)
                .withEnv(env)
                .withHostConfig(com.github.dockerjava.api.model.HostConfig.newHostConfig().withPortBindings(portBindings))
                .exec();
        return response.getId();
    }
}
