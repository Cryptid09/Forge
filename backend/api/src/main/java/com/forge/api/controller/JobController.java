package com.forge.api.controller;

import com.forge.application.job.JobManager;
import com.forge.core.job.Job;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/jobs")
@CrossOrigin("*")
public class JobController {

    private final JobManager jobManager;

    public JobController(JobManager jobManager) {
        this.jobManager = jobManager;
    }

    @GetMapping
    public List<Job> getJobs() {
        return jobManager.getJobHistory();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Job> getJob(@PathVariable String id) {
        try {
            return ResponseEntity.ok(jobManager.getJobById(id));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
