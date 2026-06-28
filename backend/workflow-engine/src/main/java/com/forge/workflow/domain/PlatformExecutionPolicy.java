package com.forge.workflow.domain;

/**
 * Platform-wide execution policy defaults.
 * These are the lowest-priority layer in the inheritance chain:
 *   Platform Defaults → Workflow Defaults → Step Overrides
 *
 * All fields must be non-null — they are the fallback of last resort.
 */
public final class PlatformExecutionPolicy {

    private PlatformExecutionPolicy() {}

    public static final ExecutionPolicy DEFAULTS = new ExecutionPolicy()
            .setContinueOnFailure(false)
            .setRetryCount(0)
            .setRetryDelayMs(1000L)
            .setTimeoutMs(0L);   // 0 = no timeout
}
