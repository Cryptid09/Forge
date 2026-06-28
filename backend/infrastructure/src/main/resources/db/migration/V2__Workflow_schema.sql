-- Make events.job_id nullable so workflow-level events (not tied to a Job) can be stored
ALTER TABLE events ALTER COLUMN job_id DROP NOT NULL;
ALTER TABLE events DROP CONSTRAINT IF EXISTS fk_events_jobs;
ALTER TABLE events ADD COLUMN workflow_execution_id VARCHAR(36);

-- Workflows
CREATE TABLE workflows (
    id                           VARCHAR(36) PRIMARY KEY,
    name                         VARCHAR(255) NOT NULL,
    description                  TEXT,
    version                      INT NOT NULL DEFAULT 1,
    enabled                      BOOLEAN NOT NULL DEFAULT TRUE,
    default_continue_on_failure  BOOLEAN NOT NULL DEFAULT FALSE,
    created_at                   TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at                   TIMESTAMP WITH TIME ZONE NOT NULL
);

-- Workflow steps (ordered list attached to a workflow)
CREATE TABLE workflow_steps (
    id                       VARCHAR(36) PRIMARY KEY,
    workflow_id              VARCHAR(36) NOT NULL,
    step_order               INT NOT NULL,
    name                     VARCHAR(255),
    tool_id                  VARCHAR(255) NOT NULL,
    parameters               TEXT,
    timeout_ms               BIGINT NOT NULL DEFAULT 0,
    retry_count              INT NOT NULL DEFAULT 0,
    retry_delay_ms           BIGINT NOT NULL DEFAULT 1000,
    continue_on_failure      BOOLEAN,
    CONSTRAINT fk_steps_workflow FOREIGN KEY (workflow_id) REFERENCES workflows(id) ON DELETE CASCADE
);

-- Workflow executions (one per trigger)
CREATE TABLE workflow_executions (
    id             VARCHAR(36) PRIMARY KEY,
    workflow_id    VARCHAR(36) NOT NULL,
    workflow_name  VARCHAR(255),
    status         VARCHAR(50) NOT NULL,
    started_at     TIMESTAMP WITH TIME ZONE NOT NULL,
    finished_at    TIMESTAMP WITH TIME ZONE,
    duration_ms    BIGINT,
    CONSTRAINT fk_wexec_workflow FOREIGN KEY (workflow_id) REFERENCES workflows(id)
);

-- Workflow step executions (one per step per attempt per workflow execution)
CREATE TABLE workflow_step_executions (
    id                     VARCHAR(36) PRIMARY KEY,
    workflow_execution_id  VARCHAR(36) NOT NULL,
    step_id                VARCHAR(36),
    step_order             INT NOT NULL,
    step_name              VARCHAR(255),
    tool_id                VARCHAR(255),
    tool_execution_job_id  VARCHAR(36),
    status                 VARCHAR(50) NOT NULL,
    started_at             TIMESTAMP WITH TIME ZONE NOT NULL,
    finished_at            TIMESTAMP WITH TIME ZONE,
    attempt_number         INT NOT NULL DEFAULT 1,
    error_message          TEXT,
    CONSTRAINT fk_step_exec_wexec FOREIGN KEY (workflow_execution_id) REFERENCES workflow_executions(id)
);
