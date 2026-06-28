package com.forge.devopstoolbox.application.service;

import com.forge.devopstoolbox.core.model.PlatformEvent;
import com.forge.devopstoolbox.core.port.EventRepository;
import java.util.List;
import java.util.UUID;

public class EventQueryService {

    private final EventRepository eventRepository;

    public EventQueryService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public List<PlatformEvent> listEvents() {
        return eventRepository.findAll();
    }

    public List<PlatformEvent> listEventsByJob(UUID jobId) {
        return eventRepository.findByJobId(jobId);
    }
}
