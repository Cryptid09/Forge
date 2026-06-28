package com.forge.infrastructure.persistence;

import com.forge.infrastructure.persistence.entity.MetricEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaMetricRepository extends JpaRepository<MetricEntity, String> {
}
