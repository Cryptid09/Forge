package com.forge.eventstreaming;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.forge.core.event.Event;
import com.forge.infrastructure.persistence.JpaAuditLogRepository;
import com.forge.infrastructure.persistence.entity.AuditLogEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class AuditConsumer {

    private static final Logger log = LoggerFactory.getLogger(AuditConsumer.class);

    private final JpaAuditLogRepository auditLogRepository;
    private final ObjectMapper objectMapper;

    public AuditConsumer(JpaAuditLogRepository auditLogRepository, ObjectMapper objectMapper) {
        this.auditLogRepository = auditLogRepository;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "audit", groupId = "forge-audit-group")
    public void consumeAudit(String payload) {
        try {
            Event event = objectMapper.readValue(payload, Event.class);
            AuditLogEntity entity = new AuditLogEntity();
            entity.setId(event.getId());
            entity.setEventType(event.getEventType());
            entity.setSource(event.getSource());
            entity.setCorrelationId(event.getCorrelationId());
            entity.setWorkflowId(event.getWorkflowId());
            entity.setJobId(event.getJobId());
            entity.setPayload(event.getPayload());
            entity.setVersion(event.getVersion());
            entity.setTimestamp(event.getTimestamp());
            auditLogRepository.save(entity);
            log.debug("Persisted audit log for event {}", event.getId());
        } catch (Exception e) {
            log.error("Failed to process audit event: {}", e.getMessage());
            throw new RuntimeException("Audit event processing failed", e); // Throwing allows Spring Kafka DLQ to catch it
        }
    }
}
