package com.forge.devopstoolbox.api.mapper;

import com.forge.devopstoolbox.api.dto.EventResponse;
import com.forge.devopstoolbox.api.dto.JobResponse;
import com.forge.devopstoolbox.api.dto.JobStatisticsResponse;
import com.forge.devopstoolbox.api.dto.ToolDescriptorResponse;
import com.forge.devopstoolbox.core.model.Job;
import com.forge.devopstoolbox.core.model.JobStatistics;
import com.forge.devopstoolbox.core.model.PlatformEvent;
import com.forge.devopstoolbox.core.tool.ToolDescriptor;

public final class PlatformMapper {

    private PlatformMapper() {
    }

    public static ToolDescriptorResponse toToolResponse(ToolDescriptor descriptor) {
        return new ToolDescriptorResponse(
                descriptor.id(),
                descriptor.name(),
                descriptor.description(),
                descriptor.category(),
                descriptor.version()
        );
    }

    public static JobResponse toJobResponse(Job job) {
        return new JobResponse(
                job.id(),
                job.toolId(),
                job.status().name(),
                job.startedAt(),
                job.finishedAt(),
                job.durationMs(),
                job.output(),
                job.errorMessage(),
                job.createdAt()
        );
    }

    public static JobStatisticsResponse toStatisticsResponse(JobStatistics statistics) {
        return new JobStatisticsResponse(
                statistics.total(),
                statistics.running(),
                statistics.completed(),
                statistics.failed(),
                statistics.queued()
        );
    }

    public static EventResponse toEventResponse(PlatformEvent event) {
        return new EventResponse(
                event.id(),
                event.eventType().name(),
                event.jobId(),
                event.toolId(),
                event.timestamp(),
                event.payload()
        );
    }
}
