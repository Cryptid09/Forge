package com.forge.devopstoolbox.application.service;

import com.forge.devopstoolbox.core.model.ApplicationInfo;

public class InfoQueryService {

    private final String name;
    private final String description;
    private final String environment;
    private final String buildTime;

    public InfoQueryService(String name, String description, String environment, String buildTime) {
        this.name = name;
        this.description = description;
        this.environment = environment;
        this.buildTime = buildTime;
    }

    public ApplicationInfo getInfo() {
        return new ApplicationInfo(name, description, environment, buildTime);
    }
}
