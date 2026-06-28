package com.forge.devopstoolbox.api.controller;

import com.forge.devopstoolbox.api.dto.VersionResponse;
import com.forge.devopstoolbox.application.service.VersionQueryService;
import com.forge.devopstoolbox.core.model.VersionInfo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/version")
class VersionController {

    private final VersionQueryService versionQueryService;

    VersionController(VersionQueryService versionQueryService) {
        this.versionQueryService = versionQueryService;
    }

    @GetMapping
    VersionResponse getVersion() {
        VersionInfo version = versionQueryService.getVersion();
        return new VersionResponse(
                version.version(),
                version.artifact(),
                version.javaVersion(),
                version.springBootVersion()
        );
    }
}
