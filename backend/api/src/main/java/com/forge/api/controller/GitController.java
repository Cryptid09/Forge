package com.forge.api.controller;

import com.forge.api.dto.GitRepositoryDto;
import com.forge.application.execution.ExecutionService;
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
@RequestMapping("/api/git")
@CrossOrigin("*")
public class GitController {

    private final GitRepositoryService gitRepositoryService;
    private final ExecutionService executionService;

    public GitController(GitRepositoryService gitRepositoryService, ExecutionService executionService) {
        this.gitRepositoryService = gitRepositoryService;
        this.executionService = executionService;
    }

    @GetMapping("/repositories")
    public List<GitRepositoryDto> listRepositories() {
        return gitRepositoryService.listAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    @PostMapping("/repositories")
    public GitRepositoryDto registerRepository(@RequestBody Map<String, String> body) {
        String name = body.get("name");
        String remoteUrl = body.get("remoteUrl");
        String localPath = body.get("localPath");
        return toDto(gitRepositoryService.register(name, remoteUrl, localPath));
    }

    @DeleteMapping("/repositories/{id}")
    public ResponseEntity<Void> unregisterRepository(@PathVariable String id) {
        gitRepositoryService.unregister(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/clone")
    public ResponseEntity<?> cloneRepository(@RequestBody Map<String, String> body) {
        String remoteUrl = body.get("remoteUrl");
        String localPath = body.get("localPath");
        
        Job job = executionService.executeToolSync("git-clone", Map.of(
            "remoteUrl", remoteUrl,
            "localPath", localPath
        ));
        
        return ResponseEntity.ok(Map.of(
            "jobId", job.getId(),
            "status", job.getStatus().name()
        ));
    }

    @PostMapping("/pull")
    public ResponseEntity<?> pullRepository(@RequestBody Map<String, String> body) {
        String repoId = body.get("repositoryId");
        Optional<GitRepository> repo = gitRepositoryService.findById(repoId);
        if (repo.isEmpty()) return ResponseEntity.badRequest().body("Repository not found");

        Job job = executionService.executeToolSync("git-pull", Map.of(
            "localPath", repo.get().getLocalPath()
        ));
        
        return ResponseEntity.ok(Map.of("jobId", job.getId(), "status", job.getStatus().name()));
    }

    @PostMapping("/checkout")
    public ResponseEntity<?> checkoutBranch(@RequestBody Map<String, String> body) {
        String repoId = body.get("repositoryId");
        String branch = body.get("branch");
        Optional<GitRepository> repo = gitRepositoryService.findById(repoId);
        if (repo.isEmpty()) return ResponseEntity.badRequest().body("Repository not found");

        Job job = executionService.executeToolSync("git-checkout", Map.of(
            "localPath", repo.get().getLocalPath(),
            "branch", branch
        ));
        
        return ResponseEntity.ok(Map.of("jobId", job.getId(), "status", job.getStatus().name()));
    }

    private GitRepositoryDto toDto(GitRepository repo) {
        return new GitRepositoryDto(repo.getId(), repo.getName(), repo.getRemoteUrl(), repo.getLocalPath(), repo.getCreatedAt());
    }
}
