package com.forge.devopstoolbox.infrastructure.persistence;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

interface JobJpaRepository extends JpaRepository<JobEntity, UUID> {

    List<JobEntity> findAllByOrderByCreatedAtDesc();

    long countByStatus(com.forge.devopstoolbox.core.model.JobStatus status);
}
