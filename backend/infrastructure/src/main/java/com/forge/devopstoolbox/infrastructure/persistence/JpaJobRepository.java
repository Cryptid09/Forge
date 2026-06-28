package com.forge.devopstoolbox.infrastructure.persistence;

import com.forge.devopstoolbox.core.model.Job;
import com.forge.devopstoolbox.core.model.JobStatistics;
import com.forge.devopstoolbox.core.model.JobStatus;
import com.forge.devopstoolbox.core.port.JobRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
class JpaJobRepository implements JobRepository {

    private final JobJpaRepository jpaRepository;

    JpaJobRepository(JobJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Job save(Job job) {
        return toDomain(jpaRepository.save(toEntity(job)));
    }

    @Override
    public Optional<Job> findById(UUID id) {
        return jpaRepository.findById(id).map(this::toDomain);
    }

    @Override
    public List<Job> findAll() {
        return jpaRepository.findAllByOrderByCreatedAtDesc().stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public JobStatistics getStatistics() {
        long total = jpaRepository.count();
        long running = jpaRepository.countByStatus(JobStatus.RUNNING);
        long completed = jpaRepository.countByStatus(JobStatus.COMPLETED);
        long failed = jpaRepository.countByStatus(JobStatus.FAILED);
        long queued = jpaRepository.countByStatus(JobStatus.QUEUED);
        return new JobStatistics(total, running, completed, failed, queued);
    }

    private JobEntity toEntity(Job job) {
        JobEntity entity = new JobEntity();
        entity.setId(job.id());
        entity.setToolId(job.toolId());
        entity.setStatus(job.status());
        entity.setStartedAt(job.startedAt());
        entity.setFinishedAt(job.finishedAt());
        entity.setDurationMs(job.durationMs());
        entity.setOutput(job.output());
        entity.setErrorMessage(job.errorMessage());
        entity.setCreatedAt(job.createdAt());
        return entity;
    }

    private Job toDomain(JobEntity entity) {
        return new Job(
                entity.getId(),
                entity.getToolId(),
                entity.getStatus(),
                entity.getStartedAt(),
                entity.getFinishedAt(),
                entity.getDurationMs(),
                entity.getOutput(),
                entity.getErrorMessage(),
                entity.getCreatedAt()
        );
    }
}
