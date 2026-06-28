package com.forge.infrastructure.persistence.entity;

import com.forge.core.event.Event;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;

@Entity
@Table(name = "events")
public class EventEntity {
    @Id
    private String id;
    private String eventType;
    private String jobId;
    private Instant timestamp;
    private String payload;

    public EventEntity() {}

    public EventEntity(Event event) {
        this.id = event.getId();
        this.eventType = event.getEventType();
        this.jobId = event.getJobId();
        this.timestamp = event.getTimestamp();
        this.payload = event.getPayload();
    }

    public Event toDomain() {
        Event event = new Event();
        event.setId(id);
        event.setEventType(eventType);
        event.setJobId(jobId);
        event.setTimestamp(timestamp);
        event.setPayload(payload);
        return event;
    }
}
