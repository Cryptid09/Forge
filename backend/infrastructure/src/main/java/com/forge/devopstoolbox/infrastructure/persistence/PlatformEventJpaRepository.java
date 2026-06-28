package com.forge.devopstoolbox.infrastructure.persistence;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

interface PlatformEventJpaRepository extends JpaRepository<PlatformEventEntity, UUID> {

    List<PlatformEventEntity> findAllByOrderByTimestampDesc();

    List<PlatformEventEntity> findByJobIdOrderByTimestampAsc(UUID jobId);
}
