package com.forge.api.controller;

import com.forge.api.dto.ArtifactDto;
import com.forge.api.dto.BuildRunRequestDto;
import com.forge.application.execution.ExecutionService;
import com.forge.build.domain.BuildArtifact;
import com.forge.build.port.BuildArtifactStore;
import com.forge.build.service.ArtifactDetector;
import com.forge.core.job.Job;
import com.forge.git.domain.GitRepository;
import com.forge.git.service.GitRepositoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/build")
@CrossOrigin("*")
public class BuildController {

    private final ExecutionService executionService;
    private final GitRepositoryService gitRepositoryService;
    private final ArtifactDetector artifactDetector;
    private final BuildArtifactStore buildArtifactStore;

    public BuildController(ExecutionService executionService, 
                           GitRepositoryService gitRepositoryService, 
                           ArtifactDetector artifactDetector, 
                           BuildArtifactStore buildArtifactStore) {
        this.executionService = executionService;
        this.gitRepositoryService = gitRepositoryService;
        this.artifactDetector = artifactDetector;
        this.buildArtifactStore = buildArtifactStore;
    }

    @PostMapping("/run")
    public ResponseEntity<?> runBuild(@RequestBody BuildRunRequestDto request) {
        Optional<GitRepository> repo = gitRepositoryService.findById(request.repositoryId());
        if (repo.isEmpty()) return ResponseEntity.badRequest().body("Repository not found");

        String toolId = request.buildTool().toLowerCase() + "-build"; // maven-build or gradle-build
        Job job = executionService.executeToolSync(toolId, Map.of(
            "localPath", repo.get().getLocalPath(),
            request.buildTool().equalsIgnoreCase("maven") ? "goals" : "tasks", request.tasks()
        ));

        // If build succeeds, run artifact detection
        if (job.getStatus() == com.forge.core.job.JobStatus.COMPLETED) {
            List<BuildArtifact> found = artifactDetector.discoverArtifacts(request.repositoryId(), repo.get().getLocalPath());
            found.forEach(buildArtifactStore::save);
        }

        return ResponseEntity.ok(Map.of("jobId", job.getId(), "status", job.getStatus().name()));
    }

    @GetMapping("/artifacts")
    public List<ArtifactDto> listArtifacts(@RequestParam(required = false) String repositoryId) {
        List<BuildArtifact> artifacts;
        if (repositoryId != null && !repositoryId.isBlank()) {
            artifacts = buildArtifactStore.findByRepositoryId(repositoryId);
        } else {
            artifacts = buildArtifactStore.findAll();
        }
        return artifacts.stream().map(this::toDto).collect(Collectors.toList());
    }

    private ArtifactDto toDto(BuildArtifact a) {
        return new ArtifactDto(a.getId(), a.getRepositoryId(), a.getName(), a.getType(), a.getSizeBytes(), a.getLocalPath(), a.getBuildTime());
    }
}
