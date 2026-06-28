CREATE TABLE audit_logs (
    id             VARCHAR(36) PRIMARY KEY,
    event_type     VARCHAR(255) NOT NULL,
    source         VARCHAR(255) NOT NULL,
    correlation_id VARCHAR(36) NOT NULL,
    workflow_id    VARCHAR(36),
    job_id         VARCHAR(36),
    payload        TEXT,
    version        INT NOT NULL,
    timestamp      TIMESTAMP WITH TIME ZONE NOT NULL
);

CREATE TABLE notifications (
    id             VARCHAR(36) PRIMARY KEY,
    message        VARCHAR(512) NOT NULL,
    source         VARCHAR(255) NOT NULL,
    severity       VARCHAR(50) NOT NULL,
    read           BOOLEAN DEFAULT FALSE,
    timestamp      TIMESTAMP WITH TIME ZONE NOT NULL
);

CREATE TABLE metrics_data (
    id             VARCHAR(36) PRIMARY KEY,
    metric_name    VARCHAR(255) NOT NULL,
    metric_value   DOUBLE PRECISION NOT NULL,
    dimensions     VARCHAR(512),
    timestamp      TIMESTAMP WITH TIME ZONE NOT NULL
);
