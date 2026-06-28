-- Log analysis results
CREATE TABLE log_analyses (
    id          VARCHAR(36) PRIMARY KEY,
    job_id      VARCHAR(36),
    source      VARCHAR(255),
    result_json TEXT NOT NULL,
    created_at  TIMESTAMP WITH TIME ZONE NOT NULL
);
