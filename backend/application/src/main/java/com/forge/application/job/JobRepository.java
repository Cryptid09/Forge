package com.forge.application.job;

import com.forge.core.job.Job;

import java.util.List;
import java.util.Optional;

public interface JobRepository {
    Job save(Job job);
    Optional<Job> findById(String id);
    List<Job> findAll();
}
