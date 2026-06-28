package com.forge.git.service;

import com.forge.git.domain.GitRepository;
import com.forge.git.port.GitRepositoryStore;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class GitRepositoryService {
    
    private final GitRepositoryStore store;

    public GitRepositoryService(GitRepositoryStore store) {
        this.store = store;
    }

    public GitRepository register(String name, String remoteUrl, String localPath) {
        GitRepository repo = new GitRepository();
        repo.setId(UUID.randomUUID().toString());
        repo.setName(name);
        repo.setRemoteUrl(remoteUrl);
        repo.setLocalPath(localPath);
        repo.setCreatedAt(Instant.now());
        return store.save(repo);
    }

    public List<GitRepository> listAll() {
        return store.findAll();
    }

    public Optional<GitRepository> findById(String id) {
        return store.findById(id);
    }

    public void unregister(String id) {
        store.deleteById(id);
    }
}
