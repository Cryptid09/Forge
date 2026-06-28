package com.forge.api.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/kafka")
@CrossOrigin("*")
public class KafkaMonitoringController {

    // Simple mock endpoints for the UI, as the real monitoring is handled by Kafka UI in Docker
    // This allows the frontend to have a basic Kafka dashboard without pulling in large AdminClient dependencies directly.

    @GetMapping("/topics")
    public List<Map<String, Object>> getTopics() {
        return List.of(
            Map.of("name", "jobs", "partitions", 3, "messages", 152),
            Map.of("name", "workflows", "partitions", 3, "messages", 42),
            Map.of("name", "tools", "partitions", 3, "messages", 563),
            Map.of("name", "audit", "partitions", 3, "messages", 757),
            Map.of("name", "notifications", "partitions", 1, "messages", 15),
            Map.of("name", "metrics", "partitions", 1, "messages", 15)
        );
    }

    @GetMapping("/consumers")
    public List<Map<String, Object>> getConsumers() {
        return List.of(
            Map.of("groupId", "forge-audit-group", "state", "Stable", "lag", 0),
            Map.of("groupId", "forge-notification-group", "state", "Stable", "lag", 0),
            Map.of("groupId", "forge-metrics-group", "state", "Stable", "lag", 0)
        );
    }
}
