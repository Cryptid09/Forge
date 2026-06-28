package com.forge.devopstoolbox.api.dto;

public record JobStatisticsResponse(
        long total,
        long running,
        long completed,
        long failed,
        long queued
) {
}
