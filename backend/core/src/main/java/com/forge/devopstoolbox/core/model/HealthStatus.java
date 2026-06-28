package com.forge.devopstoolbox.core.model;

import java.util.List;

public record HealthStatus(HealthState status, List<HealthComponent> components) {

    public record HealthComponent(String name, HealthState status) {
    }
}
