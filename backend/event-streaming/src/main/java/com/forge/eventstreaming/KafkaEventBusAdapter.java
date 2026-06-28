package com.forge.eventstreaming;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.forge.core.event.Event;
import com.forge.core.event.EventBus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Primary
public class KafkaEventBusAdapter implements EventBus {
    
    private static final Logger log = LoggerFactory.getLogger(KafkaEventBusAdapter.class);
    
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public KafkaEventBusAdapter(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @Override
    public void publish(Event event) {
        try {
            String payload = objectMapper.writeValueAsString(event);
            String topic = determineTopic(event.getEventType());
            String key = event.getCorrelationId(); // Partition by correlation ID to preserve ordering
            
            kafkaTemplate.send(topic, key, payload)
                .whenComplete((result, ex) -> {
                    if (ex == null) {
                        log.debug("Published event {} to topic {}", event.getId(), topic);
                    } else {
                        log.error("Failed to publish event {} to topic {}: {}", event.getId(), topic, ex.getMessage());
                        // In a production system, we might use an outbox pattern here
                    }
                });
                
            // All events also go to audit topic
            kafkaTemplate.send("audit", key, payload);
            
        } catch (JsonProcessingException e) {
            log.error("Failed to serialize event {}: {}", event.getId(), e.getMessage());
        }
    }
    
    private String determineTopic(String eventType) {
        if (eventType == null) return "tools";
        
        String lower = eventType.toLowerCase();
        if (lower.contains("job")) {
            return "jobs";
        } else if (lower.contains("workflow")) {
            return "workflows";
        } else if (lower.contains("metric") || lower.contains("duration")) {
            return "metrics";
        } else if (lower.contains("notification") || lower.contains("failed") || lower.contains("crashed")) {
            return "notifications";
        }
        return "tools";
    }
}
