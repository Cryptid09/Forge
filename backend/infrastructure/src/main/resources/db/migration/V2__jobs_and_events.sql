CREATE TABLE jobs (
    id UUID PRIMARY KEY,
    tool_id VARCHAR(255) NOT NULL,
    status VARCHAR(50) NOT NULL,
    started_at TIMESTAMPTZ,
    finished_at TIMESTAMPTZ,
    duration_ms BIGINT,
    output TEXT,
    error_message TEXT,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_jobs_tool_id ON jobs (tool_id);
CREATE INDEX idx_jobs_status ON jobs (status);
CREATE INDEX idx_jobs_created_at ON jobs (created_at DESC);

CREATE TABLE platform_events (
    id UUID PRIMARY KEY,
    event_type VARCHAR(100) NOT NULL,
    job_id UUID REFERENCES jobs (id),
    tool_id VARCHAR(255),
    timestamp TIMESTAMPTZ NOT NULL,
    payload TEXT
);

CREATE INDEX idx_platform_events_job_id ON platform_events (job_id);
CREATE INDEX idx_platform_events_timestamp ON platform_events (timestamp DESC);
