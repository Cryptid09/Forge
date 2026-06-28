package com.forge.core.event;

import java.time.Instant;
import java.util.UUID;

public class Event {
    private String id;
    private String eventType;
    private String source;
    private String correlationId;
    private String workflowId;
    private String jobId;
    private Instant timestamp;
    private String payload;
    private int version;

    public Event() {}

    public Event(String eventType, String jobId, String payload) {
        this.id = UUID.randomUUID().toString();
        this.eventType = eventType;
        this.source = "platform";
        this.correlationId = jobId != null ? jobId : UUID.randomUUID().toString();
        this.jobId = jobId;
        this.timestamp = Instant.now();
        this.payload = payload;
        this.version = 1;
    }

    public Event(String eventType, String source, String correlationId, String workflowId, String jobId, String payload) {
        this.id = UUID.randomUUID().toString();
        this.eventType = eventType;
        this.source = source != null ? source : "platform";
        this.correlationId = correlationId != null ? correlationId : UUID.randomUUID().toString();
        this.workflowId = workflowId;
        this.jobId = jobId;
        this.timestamp = Instant.now();
        this.payload = payload;
        this.version = 1;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getEventType() { return eventType; }
    public void setEventType(String eventType) { this.eventType = eventType; }
    
    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }

    public String getCorrelationId() { return correlationId; }
    public void setCorrelationId(String correlationId) { this.correlationId = correlationId; }

    public String getWorkflowId() { return workflowId; }
    public void setWorkflowId(String workflowId) { this.workflowId = workflowId; }

    public String getJobId() { return jobId; }
    public void setJobId(String jobId) { this.jobId = jobId; }
    
    public Instant getTimestamp() { return timestamp; }
    public void setTimestamp(Instant timestamp) { this.timestamp = timestamp; }
    
    public String getPayload() { return payload; }
    public void setPayload(String payload) { this.payload = payload; }

    public int getVersion() { return version; }
    public void setVersion(int version) { this.version = version; }
}
