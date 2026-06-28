-- Replace the old workflow_steps table with a new schema that stores the
-- execution policy as a JSON column, so new policy fields can be added
-- without schema changes.

ALTER TABLE workflows
    DROP COLUMN IF EXISTS default_continue_on_failure;

ALTER TABLE workflows
    ADD COLUMN execution_policy_json TEXT;

-- workflow_steps: drop the individual policy columns, replace with JSON
ALTER TABLE workflow_steps
    DROP COLUMN IF EXISTS timeout_ms;
ALTER TABLE workflow_steps
    DROP COLUMN IF EXISTS retry_count;
ALTER TABLE workflow_steps
    DROP COLUMN IF EXISTS retry_delay_ms;
ALTER TABLE workflow_steps
    DROP COLUMN IF EXISTS continue_on_failure;

ALTER TABLE workflow_steps
    ADD COLUMN execution_policy_json TEXT;
