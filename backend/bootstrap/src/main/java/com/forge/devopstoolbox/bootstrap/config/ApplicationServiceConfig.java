package com.forge.devopstoolbox.bootstrap.config;

import com.forge.devopstoolbox.application.service.HealthQueryService;
import com.forge.devopstoolbox.application.service.InfoQueryService;
import com.forge.devopstoolbox.application.service.VersionQueryService;
import com.forge.devopstoolbox.core.port.SchemaMetadataRepository;
import org.springframework.boot.SpringBootVersion;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(AppProperties.class)
class ApplicationServiceConfig {

    @Bean
    HealthQueryService healthQueryService(SchemaMetadataRepository schemaMetadataRepository) {
        return new HealthQueryService(schemaMetadataRepository);
    }

    @Bean
    InfoQueryService infoQueryService(AppProperties appProperties) {
        return new InfoQueryService(
                appProperties.getName(),
                appProperties.getDescription(),
                appProperties.getEnvironment(),
                appProperties.getBuildTime()
        );
    }

    @Bean
    VersionQueryService versionQueryService(AppProperties appProperties) {
        return new VersionQueryService(
                appProperties.getVersion(),
                appProperties.getArtifact(),
                System.getProperty("java.version"),
                SpringBootVersion.getVersion()
        );
    }
}
