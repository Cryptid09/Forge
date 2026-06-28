package com.forge.devopstoolbox.api.controller;

import com.forge.devopstoolbox.api.dto.InfoResponse;
import com.forge.devopstoolbox.application.service.InfoQueryService;
import com.forge.devopstoolbox.core.model.ApplicationInfo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/info")
class InfoController {

    private final InfoQueryService infoQueryService;

    InfoController(InfoQueryService infoQueryService) {
        this.infoQueryService = infoQueryService;
    }

    @GetMapping
    InfoResponse getInfo() {
        ApplicationInfo info = infoQueryService.getInfo();
        return new InfoResponse(info.name(), info.description(), info.environment(), info.buildTime());
    }
}
