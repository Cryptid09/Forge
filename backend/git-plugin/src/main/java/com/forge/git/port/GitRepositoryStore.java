package com.forge.git.port;

import com.forge.git.domain.GitRepository;
import java.util.List;
import java.util.Optional;

public interface GitRepositoryStore {
    GitRepository save(GitRepository repository);
    Optional<GitRepository> findById(String id);
    List<GitRepository> findAll();
    void deleteById(String id);
}
