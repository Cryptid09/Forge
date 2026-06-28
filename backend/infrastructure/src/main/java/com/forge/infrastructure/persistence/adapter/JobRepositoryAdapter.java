package com.forge.infrastructure.persistence.adapter;

import com.forge.application.job.JobRepository;
import com.forge.core.job.Job;
import com.forge.infrastructure.persistence.entity.JobEntity;
import com.forge.infrastructure.persistence.repository.JpaJobRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class JobRepositoryAdapter implements JobRepository {

    private final JpaJobRepository jpaJobRepository;

    public JobRepositoryAdapter(JpaJobRepository jpaJobRepository) {
        this.jpaJobRepository = jpaJobRepository;
    }

    @Override
    public Job save(Job job) {
        JobEntity entity = new JobEntity(job);
        return jpaJobRepository.save(entity).toDomain();
    }

    @Override
    public Optional<Job> findById(String id) {
        return jpaJobRepository.findById(id).map(JobEntity::toDomain);
    }

    @Override
    public List<Job> findAll() {
        return jpaJobRepository.findAll().stream().map(JobEntity::toDomain).toList();
    }
}
