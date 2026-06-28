package com.forge.devopstoolbox.api.dto;

import java.util.Map;

public record ExecuteToolRequest(
        Map<String, String> parameters
) {

    public ExecuteToolRequest {
        if (parameters == null) {
            parameters = Map.of();
        }
    }
}
