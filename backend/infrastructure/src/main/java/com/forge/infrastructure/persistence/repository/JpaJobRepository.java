package com.forge.infrastructure.persistence.repository;

import com.forge.infrastructure.persistence.entity.JobEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaJobRepository extends JpaRepository<JobEntity, String> {
}
