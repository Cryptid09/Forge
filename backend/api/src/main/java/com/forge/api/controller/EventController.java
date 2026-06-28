package com.forge.api.controller;

import com.forge.api.dto.AuditLogDto;
import com.forge.api.dto.MetricDto;
import com.forge.api.dto.NotificationDto;
import com.forge.infrastructure.persistence.JpaAuditLogRepository;
import com.forge.infrastructure.persistence.JpaMetricRepository;
import com.forge.infrastructure.persistence.JpaNotificationRepository;
import com.forge.infrastructure.persistence.entity.AuditLogEntity;
import com.forge.infrastructure.persistence.entity.MetricEntity;
import com.forge.infrastructure.persistence.entity.NotificationEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@CrossOrigin("*")
public class EventController {

    private final JpaAuditLogRepository auditLogRepository;
    private final JpaMetricRepository metricRepository;
    private final JpaNotificationRepository notificationRepository;

    public EventController(JpaAuditLogRepository auditLogRepository, 
                           JpaMetricRepository metricRepository, 
                           JpaNotificationRepository notificationRepository) {
        this.auditLogRepository = auditLogRepository;
        this.metricRepository = metricRepository;
        this.notificationRepository = notificationRepository;
    }

    @GetMapping("/events")
    public List<AuditLogDto> getAllEvents() {
        return auditLogRepository.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    @GetMapping("/events/{id}")
    public AuditLogDto getEventById(@PathVariable String id) {
        return auditLogRepository.findById(id).map(this::toDto).orElse(null);
    }

    @GetMapping("/audit")
    public List<AuditLogDto> getAuditLogs() {
        return auditLogRepository.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    @GetMapping("/metrics")
    public List<MetricDto> getMetrics() {
        return metricRepository.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    @GetMapping("/notifications")
    public List<NotificationDto> getNotifications() {
        return notificationRepository.findAllByOrderByTimestampDesc().stream().map(this::toDto).collect(Collectors.toList());
    }

    private AuditLogDto toDto(AuditLogEntity e) {
        return new AuditLogDto(e.getId(), e.getEventType(), e.getSource(), e.getCorrelationId(), e.getWorkflowId(), e.getJobId(), e.getPayload(), e.getVersion(), e.getTimestamp());
    }
    
    private MetricDto toDto(MetricEntity e) {
        return new MetricDto(e.getId(), e.getMetricName(), e.getMetricValue(), e.getDimensions(), e.getTimestamp());
    }
    
    private NotificationDto toDto(NotificationEntity e) {
        return new NotificationDto(e.getId(), e.getMessage(), e.getSource(), e.getSeverity(), e.isRead(), e.getTimestamp());
    }
}
