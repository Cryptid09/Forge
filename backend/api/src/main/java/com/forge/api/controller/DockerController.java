package com.forge.api.controller;

import com.forge.application.execution.ExecutionService;
import com.forge.core.job.Job;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/docker")
@CrossOrigin("*")
public class DockerController {

    private final ExecutionService executionService;

    public DockerController(ExecutionService executionService) {
        this.executionService = executionService;
    }

    @GetMapping("/containers")
    public ResponseEntity<Job> listContainers() {
        Job job = executionService.executeToolAsync("docker-list-containers", Map.of());
        return ResponseEntity.accepted().body(job);
    }

    @PostMapping("/containers/start")
    public ResponseEntity<Job> startContainer(@RequestBody Map<String, String> body) {
        Job job = executionService.executeToolAsync("docker-start-container", body);
        return ResponseEntity.accepted().body(job);
    }

    @PostMapping("/containers/stop")
    public ResponseEntity<Job> stopContainer(@RequestBody Map<String, String> body) {
        Job job = executionService.executeToolAsync("docker-stop-container", body);
        return ResponseEntity.accepted().body(job);
    }

    @PostMapping("/containers/restart")
    public ResponseEntity<Job> restartContainer(@RequestBody Map<String, String> body) {
        Job job = executionService.executeToolAsync("docker-restart-container", body);
        return ResponseEntity.accepted().body(job);
    }

    @DeleteMapping("/containers/{id}")
    public ResponseEntity<Job> removeContainer(@PathVariable String id) {
        Job job = executionService.executeToolAsync("docker-remove-container", Map.of("containerId", id));
        return ResponseEntity.accepted().body(job);
    }

    @GetMapping("/images")
    public ResponseEntity<Job> listImages() {
        Job job = executionService.executeToolAsync("docker-list-images", Map.of());
        return ResponseEntity.accepted().body(job);
    }

    @PostMapping("/images/pull")
    public ResponseEntity<Job> pullImage(@RequestBody Map<String, String> body) {
        Job job = executionService.executeToolAsync("docker-pull-image", body);
        return ResponseEntity.accepted().body(job);
    }

    @PostMapping("/images/build")
    public ResponseEntity<Job> buildImage(@RequestBody Map<String, String> body) {
        Job job = executionService.executeToolAsync("docker-build-image", body);
        return ResponseEntity.accepted().body(job);
    }
}
