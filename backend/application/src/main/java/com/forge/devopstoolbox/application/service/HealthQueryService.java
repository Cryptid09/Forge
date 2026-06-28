package com.forge.devopstoolbox.application.service;

import com.forge.devopstoolbox.core.model.ApplicationInfo;
import com.forge.devopstoolbox.core.model.HealthState;
import com.forge.devopstoolbox.core.model.HealthStatus;
import com.forge.devopstoolbox.core.model.VersionInfo;
import com.forge.devopstoolbox.core.port.SchemaMetadataRepository;
import java.util.List;

public class HealthQueryService {

    private final SchemaMetadataRepository schemaMetadataRepository;

    public HealthQueryService(SchemaMetadataRepository schemaMetadataRepository) {
        this.schemaMetadataRepository = schemaMetadataRepository;
    }

    public HealthStatus getHealth() {
        HealthState databaseStatus = checkDatabase();
        HealthState overallStatus = databaseStatus == HealthState.UP ? HealthState.UP : HealthState.DOWN;

        return new HealthStatus(
                overallStatus,
                List.of(new HealthStatus.HealthComponent("database", databaseStatus))
        );
    }

    private HealthState checkDatabase() {
        try {
            schemaMetadataRepository.findAll();
            return HealthState.UP;
        } catch (RuntimeException ex) {
            return HealthState.DOWN;
        }
    }
}
