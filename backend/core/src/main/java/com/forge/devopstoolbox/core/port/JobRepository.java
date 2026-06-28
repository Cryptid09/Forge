package com.forge.devopstoolbox.core.port;

import com.forge.devopstoolbox.core.model.Job;
import com.forge.devopstoolbox.core.model.JobStatistics;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface JobRepository {

    Job save(Job job);

    Optional<Job> findById(UUID id);

    List<Job> findAll();

    JobStatistics getStatistics();
}
