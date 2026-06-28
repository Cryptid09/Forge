package com.forge.devopstoolbox.api.dto;

import com.forge.devopstoolbox.core.model.HealthState;
import java.util.List;

public record HealthResponse(String status, List<ComponentResponse> components) {

    public record ComponentResponse(String name, String status) {
    }

    public static HealthResponse from(HealthState status, List<ComponentResponse> components) {
        return new HealthResponse(status.name(), components);
    }
}
