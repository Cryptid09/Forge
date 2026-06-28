package com.forge.devopstoolbox.infrastructure.persistence;

import com.forge.devopstoolbox.core.model.PlatformEvent;
import com.forge.devopstoolbox.core.port.EventRepository;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
class JpaEventRepository implements EventRepository {

    private final PlatformEventJpaRepository jpaRepository;

    JpaEventRepository(PlatformEventJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public PlatformEvent save(PlatformEvent event) {
        return toDomain(jpaRepository.save(toEntity(event)));
    }

    @Override
    public List<PlatformEvent> findAll() {
        return jpaRepository.findAllByOrderByTimestampDesc().stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public List<PlatformEvent> findByJobId(UUID jobId) {
        return jpaRepository.findByJobIdOrderByTimestampAsc(jobId).stream()
                .map(this::toDomain)
                .toList();
    }

    private PlatformEventEntity toEntity(PlatformEvent event) {
        PlatformEventEntity entity = new PlatformEventEntity();
        entity.setId(event.id());
        entity.setEventType(event.eventType());
        entity.setJobId(event.jobId());
        entity.setToolId(event.toolId());
        entity.setTimestamp(event.timestamp());
        entity.setPayload(event.payload());
        return entity;
    }

    private PlatformEvent toDomain(PlatformEventEntity entity) {
        return new PlatformEvent(
                entity.getId(),
                entity.getEventType(),
                entity.getJobId(),
                entity.getToolId(),
                entity.getTimestamp(),
                entity.getPayload()
        );
    }
}
