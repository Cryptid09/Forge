package com.forge.eventstreaming;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.forge.core.event.Event;
import com.forge.infrastructure.persistence.JpaMetricRepository;
import com.forge.infrastructure.persistence.entity.MetricEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.util.UUID;

@Service
public class MetricsConsumer {

    private static final Logger log = LoggerFactory.getLogger(MetricsConsumer.class);

    private final JpaMetricRepository metricRepository;
    private final ObjectMapper objectMapper;

    public MetricsConsumer(JpaMetricRepository metricRepository, ObjectMapper objectMapper) {
        this.metricRepository = metricRepository;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "metrics", groupId = "forge-metrics-group")
    public void consumeMetric(String payload) {
        try {
            Event event = objectMapper.readValue(payload, Event.class);
            MetricEntity entity = new MetricEntity();
            entity.setId(UUID.randomUUID().toString());
            entity.setMetricName(event.getEventType());
            
            // Basic extraction: if payload is a number, treat it as the value
            double value = 1.0;
            if (event.getPayload() != null) {
                try {
                    value = Double.parseDouble(event.getPayload());
                } catch (NumberFormatException ignored) {}
            }
            
            entity.setMetricValue(value);
            entity.setDimensions("source=" + event.getSource());
            entity.setTimestamp(event.getTimestamp() != null ? event.getTimestamp() : Instant.now());
            
            metricRepository.save(entity);
            log.debug("Persisted metric {} with value {}", entity.getMetricName(), entity.getMetricValue());
        } catch (Exception e) {
            log.error("Failed to process metrics event: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
