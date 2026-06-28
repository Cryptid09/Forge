package com.forge.devopstoolbox.api.controller;

import com.forge.devopstoolbox.api.dto.JobResponse;
import com.forge.devopstoolbox.api.dto.JobStatisticsResponse;
import com.forge.devopstoolbox.api.exception.ResourceNotFoundException;
import com.forge.devopstoolbox.api.mapper.PlatformMapper;
import com.forge.devopstoolbox.application.service.JobQueryService;
import com.forge.devopstoolbox.core.model.Job;
import java.util.List;
import java.util.UUID;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/jobs")
class JobController {

    private final JobQueryService jobQueryService;

    JobController(JobQueryService jobQueryService) {
        this.jobQueryService = jobQueryService;
    }

    @GetMapping
    List<JobResponse> listJobs() {
        return jobQueryService.listJobs().stream()
                .map(PlatformMapper::toJobResponse)
                .toList();
    }

    @GetMapping("/statistics")
    JobStatisticsResponse getStatistics() {
        return PlatformMapper.toStatisticsResponse(jobQueryService.getStatistics());
    }

    @GetMapping("/{id}")
    JobResponse getJob(@PathVariable UUID id) {
        Job job = jobQueryService.getJob(id)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found: " + id));
        return PlatformMapper.toJobResponse(job);
    }
}
