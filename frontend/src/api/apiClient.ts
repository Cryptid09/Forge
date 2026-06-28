const API_BASE_URL = 'http://localhost:8080/api';

export interface ToolDescriptor {
    id: string;
    name: string;
    description: string;
    category: string;
    version: string;
}

export interface Job {
    id: string;
    toolId: string;
    status: string;
    startedAt: string;
    finishedAt: string | null;
    duration: string | null;
    output: string | null;
    errorMessage: string | null;
}

export interface Event {
    id: string;
    eventType: string;
    jobId: string;
    timestamp: string;
    payload: string;
}

export const fetchTools = async (): Promise<ToolDescriptor[]> => {
    const res = await fetch(`${API_BASE_URL}/tools`);
    if (!res.ok) throw new Error('Failed to fetch tools');
    return res.json();
};

export const executeTool = async (toolId: string, parameters: Record<string, string>): Promise<Job> => {
    const res = await fetch(`${API_BASE_URL}/tools/${toolId}/execute`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ parameters }),
    });
    if (!res.ok) throw new Error('Failed to execute tool');
    return res.json();
};

export const fetchJobs = async (): Promise<Job[]> => {
    const res = await fetch(`${API_BASE_URL}/jobs`);
    if (!res.ok) throw new Error('Failed to fetch jobs');
    return res.json();
};

export const fetchEvents = async (): Promise<Event[]> => {
    const res = await fetch(`${API_BASE_URL}/events`);
    if (!res.ok) throw new Error('Failed to fetch events');
    return res.json();
};

const apiClient = {
    get: async (url: string) => {
        const res = await fetch(`http://localhost:8080${url}`);
        if (!res.ok) throw new Error(`Failed to fetch ${url}`);
        return res.json();
    },
    post: async (url: string, body: any) => {
        const res = await fetch(`http://localhost:8080${url}`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(body)
        });
        if (!res.ok) throw new Error(`Failed to post to ${url}`);
        return res.json();
    },
    delete: async (url: string) => {
        const res = await fetch(`http://localhost:8080${url}`, {
            method: 'DELETE'
        });
        if (!res.ok) throw new Error(`Failed to delete ${url}`);
        return res.json();
    }
};

export default apiClient;
