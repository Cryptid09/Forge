const API_BASE = import.meta.env.VITE_API_BASE ?? ''

export interface HealthResponse {
  status: string
  components: Array<{ name: string; status: string }>
}

export interface InfoResponse {
  name: string
  description: string
  environment: string
  buildTime: string
}

export interface VersionResponse {
  version: string
  artifact: string
  javaVersion: string
  springBootVersion: string
}

export interface ToolDescriptor {
  id: string
  name: string
  description: string
  category: string
  version: string
}

export interface Job {
  id: string
  toolId: string
  status: string
  startedAt: string | null
  finishedAt: string | null
  durationMs: number | null
  output: string | null
  errorMessage: string | null
  createdAt: string
}

export interface JobStatistics {
  total: number
  running: number
  completed: number
  failed: number
  queued: number
}

export interface PlatformEvent {
  id: string
  eventType: string
  jobId: string
  toolId: string
  timestamp: string
  payload: string | null
}

async function fetchJson<T>(path: string, init?: RequestInit): Promise<T> {
  const response = await fetch(`${API_BASE}${path}`, init)
  if (!response.ok) {
    throw new Error(`Request failed: ${response.status} ${response.statusText}`)
  }
  return response.json() as Promise<T>
}

export const api = {
  getHealth: () => fetchJson<HealthResponse>('/api/health'),
  getInfo: () => fetchJson<InfoResponse>('/api/info'),
  getVersion: () => fetchJson<VersionResponse>('/api/version'),
  getTools: () => fetchJson<ToolDescriptor[]>('/api/tools'),
  getTool: (id: string) => fetchJson<ToolDescriptor>(`/api/tools/${id}`),
  executeTool: (id: string, parameters: Record<string, string>) =>
    fetchJson<Job>(`/api/tools/${id}/execute`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ parameters }),
    }),
  getJobs: () => fetchJson<Job[]>('/api/jobs'),
  getJob: (id: string) => fetchJson<Job>(`/api/jobs/${id}`),
  getJobStatistics: () => fetchJson<JobStatistics>('/api/jobs/statistics'),
  getEvents: () => fetchJson<PlatformEvent[]>('/api/events'),
}
