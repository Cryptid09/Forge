package com.forge.application.job;

import com.forge.core.job.Job;
import com.forge.core.job.JobStatus;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

@Service
public class JobManager {

    private final JobRepository jobRepository;

    public JobManager(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    public Job createJob(String toolId) {
        Job job = new Job(toolId);
        return jobRepository.save(job);
    }

    public Job updateStatus(String jobId, JobStatus status) {
        Job job = jobRepository.findById(jobId).orElseThrow(() -> new IllegalArgumentException("Job not found"));
        job.setStatus(status);
        return jobRepository.save(job);
    }

    public Job finishJob(String jobId, String output, Duration duration) {
        Job job = jobRepository.findById(jobId).orElseThrow(() -> new IllegalArgumentException("Job not found"));
        job.setStatus(JobStatus.COMPLETED);
        job.setOutput(output);
        job.setDuration(duration);
        job.setFinishedAt(Instant.now());
        return jobRepository.save(job);
    }

    public Job failJob(String jobId, String errorMessage, Duration duration) {
        Job job = jobRepository.findById(jobId).orElseThrow(() -> new IllegalArgumentException("Job not found"));
        job.setStatus(JobStatus.FAILED);
        job.setErrorMessage(errorMessage);
        job.setDuration(duration);
        job.setFinishedAt(Instant.now());
        return jobRepository.save(job);
    }

    public List<Job> getJobHistory() {
        return jobRepository.findAll();
    }
    
    public Job getJobById(String id) {
        return jobRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Job not found"));
    }
}
