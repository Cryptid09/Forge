package com.forge.devopstoolbox.core.port;

import com.forge.devopstoolbox.core.model.PlatformEvent;
import java.util.List;
import java.util.UUID;

public interface EventRepository {

    PlatformEvent save(PlatformEvent event);

    List<PlatformEvent> findAll();

    List<PlatformEvent> findByJobId(UUID jobId);
}
