CREATE TABLE jobs (
    id VARCHAR(36) PRIMARY KEY,
    tool_id VARCHAR(255) NOT NULL,
    status VARCHAR(50) NOT NULL,
    started_at TIMESTAMP WITH TIME ZONE NOT NULL,
    finished_at TIMESTAMP WITH TIME ZONE,
    duration_ms BIGINT,
    output TEXT,
    error_message TEXT
);

CREATE TABLE events (
    id VARCHAR(36) PRIMARY KEY,
    event_type VARCHAR(255) NOT NULL,
    job_id VARCHAR(36) NOT NULL,
    timestamp TIMESTAMP WITH TIME ZONE NOT NULL,
    payload TEXT,
    CONSTRAINT fk_events_jobs FOREIGN KEY (job_id) REFERENCES jobs(id)
);
