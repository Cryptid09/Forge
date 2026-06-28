package com.forge.devopstoolbox.bootstrap.config;

import com.forge.devopstoolbox.application.service.EventQueryService;
import com.forge.devopstoolbox.application.service.ExecutionService;
import com.forge.devopstoolbox.application.service.JobManager;
import com.forge.devopstoolbox.application.service.JobQueryService;
import com.forge.devopstoolbox.application.service.ToolQueryService;
import com.forge.devopstoolbox.core.port.EventBus;
import com.forge.devopstoolbox.core.port.EventRepository;
import com.forge.devopstoolbox.core.port.JobRepository;
import com.forge.devopstoolbox.core.port.ToolRegistry;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class PlatformServiceConfig {

    @Bean
    Executor platformTaskExecutor() {
        return Executors.newVirtualThreadPerTaskExecutor();
    }

    @Bean
    JobManager jobManager(JobRepository jobRepository, EventBus eventBus) {
        return new JobManager(jobRepository, eventBus);
    }

    @Bean
    ExecutionService executionService(
            ToolRegistry toolRegistry,
            JobManager jobManager,
            Executor platformTaskExecutor
    ) {
        return new ExecutionService(toolRegistry, jobManager, platformTaskExecutor);
    }

    @Bean
    ToolQueryService toolQueryService(ToolRegistry toolRegistry) {
        return new ToolQueryService(toolRegistry);
    }

    @Bean
    JobQueryService jobQueryService(JobRepository jobRepository) {
        return new JobQueryService(jobRepository);
    }

    @Bean
    EventQueryService eventQueryService(EventRepository eventRepository) {
        return new EventQueryService(eventRepository);
    }
}
