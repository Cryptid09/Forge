package com.forge.workflow.domain;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * A generic, map-backed execution policy.
 *
 * <p>Fields are stored by name in a plain Map. A {@code null} value for a key means
 * "not set at this layer — inherit from a lower-priority layer."
 *
 * <p><b>Resolution algorithm</b> (lowest → highest priority):
 * <pre>
 *   Platform Defaults → Workflow Defaults → Step Overrides
 * </pre>
 *
 * <p>Merging is implemented once in {@link #merge(ExecutionPolicy...)} and never needs
 * to change when new policy fields are added. To add a new field:
 * <ol>
 *   <li>Add a constant key name.</li>
 *   <li>Add a typed accessor and a builder-style setter.</li>
 *   <li>Add the platform default in {@link PlatformExecutionPolicy}.</li>
 * </ol>
 *
 * <h3>Well-known field keys</h3>
 * <ul>
 *   <li>{@link #CONTINUE_ON_FAILURE} – {@code Boolean}</li>
 *   <li>{@link #RETRY_COUNT} – {@code Integer}</li>
 *   <li>{@link #RETRY_DELAY_MS} – {@code Long}</li>
 *   <li>{@link #TIMEOUT_MS} – {@code Long} (0 = no timeout)</li>
 * </ul>
 */
public final class ExecutionPolicy {

    // ── Well-known field keys ────────────────────────────────────────────────
    public static final String CONTINUE_ON_FAILURE = "continueOnFailure";
    public static final String RETRY_COUNT         = "retryCount";
    public static final String RETRY_DELAY_MS      = "retryDelayMs";
    public static final String TIMEOUT_MS          = "timeoutMs";

    private final Map<String, Object> values;

    /** Creates an empty policy (all fields unset / inherit from parent). */
    public ExecutionPolicy() {
        this.values = new LinkedHashMap<>();
    }

    private ExecutionPolicy(Map<String, Object> values) {
        this.values = new LinkedHashMap<>(values);
    }

    // ── Generic resolution ───────────────────────────────────────────────────

    /**
     * Merges one or more policy layers in ascending priority order
     * (first argument = lowest priority, last = highest priority).
     * For each key, the highest-priority non-null value wins.
     * The algorithm never needs to change when new fields are added.
     */
    public static ExecutionPolicy merge(ExecutionPolicy... layers) {
        Map<String, Object> merged = new LinkedHashMap<>();
        for (ExecutionPolicy layer : layers) {
            if (layer == null) continue;
            layer.values.forEach((k, v) -> {
                if (v != null) merged.put(k, v);
            });
        }
        return new ExecutionPolicy(merged);
    }

    // ── Typed accessors ──────────────────────────────────────────────────────

    public Boolean getContinueOnFailure() {
        return (Boolean) values.get(CONTINUE_ON_FAILURE);
    }

    public Integer getRetryCount() {
        return (Integer) values.get(RETRY_COUNT);
    }

    public Long getRetryDelayMs() {
        Object v = values.get(RETRY_DELAY_MS);
        if (v instanceof Integer i) return i.longValue();
        return (Long) v;
    }

    public Long getTimeoutMs() {
        Object v = values.get(TIMEOUT_MS);
        if (v instanceof Integer i) return i.longValue();
        return (Long) v;
    }

    // ── Typed setters (return this for chaining) ─────────────────────────────

    public ExecutionPolicy setContinueOnFailure(Boolean v) { values.put(CONTINUE_ON_FAILURE, v); return this; }
    public ExecutionPolicy setRetryCount(Integer v)         { values.put(RETRY_COUNT, v);         return this; }
    public ExecutionPolicy setRetryDelayMs(Long v)          { values.put(RETRY_DELAY_MS, v);      return this; }
    public ExecutionPolicy setTimeoutMs(Long v)             { values.put(TIMEOUT_MS, v);          return this; }

    /** Raw access for serialisation. */
    public Map<String, Object> toMap()              { return Collections.unmodifiableMap(values); }
    public static ExecutionPolicy fromMap(Map<String, Object> m) { return new ExecutionPolicy(m != null ? m : Map.of()); }
}
