package com.forge.devopstoolbox.core.model;

public record JobStatistics(
        long total,
        long running,
        long completed,
        long failed,
        long queued
) {
}
