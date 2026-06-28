package com.forge.devopstoolbox.api.controller;

import com.forge.devopstoolbox.api.dto.HealthResponse;
import com.forge.devopstoolbox.application.service.HealthQueryService;
import com.forge.devopstoolbox.core.model.HealthStatus;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/health")
class HealthController {

    private final HealthQueryService healthQueryService;

    HealthController(HealthQueryService healthQueryService) {
        this.healthQueryService = healthQueryService;
    }

    @GetMapping
    ResponseEntity<HealthResponse> getHealth() {
        HealthStatus health = healthQueryService.getHealth();
        List<HealthResponse.ComponentResponse> components = health.components().stream()
                .map(component -> new HealthResponse.ComponentResponse(
                        component.name(),
                        component.status().name()
                ))
                .toList();

        return ResponseEntity.ok(HealthResponse.from(health.status(), components));
    }
}
