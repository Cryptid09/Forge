package com.forge.eventstreaming;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.forge.core.event.Event;
import com.forge.infrastructure.persistence.JpaNotificationRepository;
import com.forge.infrastructure.persistence.entity.NotificationEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.util.UUID;

@Service
public class NotificationConsumer {

    private static final Logger log = LoggerFactory.getLogger(NotificationConsumer.class);

    private final JpaNotificationRepository notificationRepository;
    private final ObjectMapper objectMapper;

    public NotificationConsumer(JpaNotificationRepository notificationRepository, ObjectMapper objectMapper) {
        this.notificationRepository = notificationRepository;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "notifications", groupId = "forge-notification-group")
    public void consumeNotification(String payload) {
        try {
            Event event = objectMapper.readValue(payload, Event.class);
            NotificationEntity entity = new NotificationEntity();
            entity.setId(UUID.randomUUID().toString());
            
            // Format message based on event type
            String msg = "System Notification: " + event.getEventType();
            if (event.getPayload() != null && !event.getPayload().isBlank()) {
                msg += " - " + event.getPayload();
            }
            if (msg.length() > 512) msg = msg.substring(0, 509) + "...";
            
            entity.setMessage(msg);
            entity.setSource(event.getSource());
            entity.setSeverity(event.getEventType().toLowerCase().contains("fail") ? "ERROR" : "INFO");
            entity.setRead(false);
            entity.setTimestamp(event.getTimestamp() != null ? event.getTimestamp() : Instant.now());
            
            notificationRepository.save(entity);
            log.info("Generated notification: {}", msg);
        } catch (Exception e) {
            log.error("Failed to process notification event: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
