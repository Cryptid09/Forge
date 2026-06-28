package com.forge.devopstoolbox.application.service;

import com.forge.devopstoolbox.core.model.Job;
import com.forge.devopstoolbox.core.model.JobStatistics;
import com.forge.devopstoolbox.core.port.JobRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class JobQueryService {

    private final JobRepository jobRepository;

    public JobQueryService(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    public List<Job> listJobs() {
        return jobRepository.findAll();
    }

    public Optional<Job> getJob(UUID id) {
        return jobRepository.findById(id);
    }

    public JobStatistics getStatistics() {
        return jobRepository.getStatistics();
    }
}
