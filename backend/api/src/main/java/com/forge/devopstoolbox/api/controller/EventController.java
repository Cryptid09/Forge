package com.forge.devopstoolbox.api.controller;

import com.forge.devopstoolbox.api.dto.EventResponse;
import com.forge.devopstoolbox.api.mapper.PlatformMapper;
import com.forge.devopstoolbox.application.service.EventQueryService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/events")
class EventController {

    private final EventQueryService eventQueryService;

    EventController(EventQueryService eventQueryService) {
        this.eventQueryService = eventQueryService;
    }

    @GetMapping
    List<EventResponse> listEvents() {
        return eventQueryService.listEvents().stream()
                .map(PlatformMapper::toEventResponse)
                .toList();
    }
}
