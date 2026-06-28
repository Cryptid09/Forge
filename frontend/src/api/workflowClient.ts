export interface WorkflowStep {
  id?: string;
  stepOrder: number;
  name: string;
  toolId: string;
  parameters: Record<string, string>;
  timeoutMs: number;
  retryCount: number;
  retryDelayMs: number;
  continueOnFailure?: boolean;
}

export interface Workflow {
  id: string;
  name: string;
  description: string;
  version: number;
  enabled: boolean;
  defaultContinueOnFailure: boolean;
  createdAt: string;
  updatedAt: string;
  steps: WorkflowStep[];
}

export interface WorkflowStepExecution {
  id: string;
  workflowExecutionId: string;
  stepId: string;
  stepOrder: number;
  stepName: string;
  toolId: string;
  toolExecutionJobId: string;
  status: string;
  startedAt: string;
  finishedAt: string;
  attemptNumber: number;
  errorMessage: string;
}

export interface WorkflowExecution {
  id: string;
  workflowId: string;
  workflowName: string;
  status: string;
  startedAt: string;
  finishedAt: string;
  durationMs: number;
  stepExecutions: WorkflowStepExecution[];
}

const API_BASE_URL = 'http://localhost:8080/api';

export const fetchWorkflows = async (): Promise<Workflow[]> => {
  const res = await fetch(`${API_BASE_URL}/workflows`);
  if (!res.ok) throw new Error('Failed to fetch workflows');
  return res.json();
};

export const fetchWorkflow = async (id: string): Promise<Workflow> => {
  const res = await fetch(`${API_BASE_URL}/workflows/${id}`);
  if (!res.ok) throw new Error('Failed to fetch workflow');
  return res.json();
};

export const createWorkflow = async (workflow: Partial<Workflow>): Promise<Workflow> => {
  const res = await fetch(`${API_BASE_URL}/workflows`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(workflow),
  });
  if (!res.ok) throw new Error('Failed to create workflow');
  return res.json();
};

export const importYamlWorkflow = async (yaml: string): Promise<Workflow> => {
  const res = await fetch(`${API_BASE_URL}/workflows/import/yaml`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ yaml }),
  });
  if (!res.ok) {
    const error = await res.json();
    throw new Error(error.error || 'Failed to import YAML');
  }
  return res.json();
};

export const deleteWorkflow = async (id: string): Promise<void> => {
  const res = await fetch(`${API_BASE_URL}/workflows/${id}`, { method: 'DELETE' });
  if (!res.ok) throw new Error('Failed to delete workflow');
};

export const executeWorkflow = async (id: string): Promise<WorkflowExecution> => {
  const res = await fetch(`${API_BASE_URL}/workflows/${id}/execute`, { method: 'POST' });
  if (!res.ok) throw new Error('Failed to execute workflow');
  return res.json();
};

export const fetchWorkflowExecutions = async (): Promise<WorkflowExecution[]> => {
  const res = await fetch(`${API_BASE_URL}/workflow-executions`);
  if (!res.ok) throw new Error('Failed to fetch executions');
  return res.json();
};

export const fetchWorkflowExecution = async (id: string): Promise<WorkflowExecution> => {
  const res = await fetch(`${API_BASE_URL}/workflow-executions/${id}`);
  if (!res.ok) throw new Error('Failed to fetch execution');
  return res.json();
};
