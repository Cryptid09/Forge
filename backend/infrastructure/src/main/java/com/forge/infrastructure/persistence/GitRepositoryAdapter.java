package com.forge.infrastructure.persistence;

import com.forge.git.domain.GitRepository;
import com.forge.git.port.GitRepositoryStore;
import com.forge.infrastructure.persistence.entity.GitRepositoryEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class GitRepositoryAdapter implements GitRepositoryStore {

    private final JpaGitRepository jpaRepository;

    public GitRepositoryAdapter(JpaGitRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public GitRepository save(GitRepository repository) {
        GitRepositoryEntity entity = new GitRepositoryEntity();
        entity.setId(repository.getId());
        entity.setName(repository.getName());
        entity.setRemoteUrl(repository.getRemoteUrl());
        entity.setLocalPath(repository.getLocalPath());
        entity.setCreatedAt(repository.getCreatedAt());
        jpaRepository.save(entity);
        return repository;
    }

    @Override
    public Optional<GitRepository> findById(String id) {
        return jpaRepository.findById(id).map(this::toDomain);
    }

    @Override
    public List<GitRepository> findAll() {
        return jpaRepository.findAll().stream().map(this::toDomain).collect(Collectors.toList());
    }

    @Override
    public void deleteById(String id) {
        jpaRepository.deleteById(id);
    }

    private GitRepository toDomain(GitRepositoryEntity entity) {
        GitRepository repo = new GitRepository();
        repo.setId(entity.getId());
        repo.setName(entity.getName());
        repo.setRemoteUrl(entity.getRemoteUrl());
        repo.setLocalPath(entity.getLocalPath());
        repo.setCreatedAt(entity.getCreatedAt());
        return repo;
    }
}
