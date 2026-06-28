package com.forge.devopstoolbox.infrastructure.persistence;

import com.forge.devopstoolbox.core.model.EventType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "platform_events")
class PlatformEventEntity {

    @Id
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(name = "event_type", nullable = false)
    private EventType eventType;

    @Column(name = "job_id")
    private UUID jobId;

    @Column(name = "tool_id")
    private String toolId;

    @Column(nullable = false)
    private Instant timestamp;

    @Column(columnDefinition = "TEXT")
    private String payload;

    protected PlatformEventEntity() {
    }

    UUID getId() {
        return id;
    }

    EventType getEventType() {
        return eventType;
    }

    UUID getJobId() {
        return jobId;
    }

    String getToolId() {
        return toolId;
    }

    Instant getTimestamp() {
        return timestamp;
    }

    String getPayload() {
        return payload;
    }

    void setId(UUID id) {
        this.id = id;
    }

    void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    void setJobId(UUID jobId) {
        this.jobId = jobId;
    }

    void setToolId(String toolId) {
        this.toolId = toolId;
    }

    void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    void setPayload(String payload) {
        this.payload = payload;
    }
}
