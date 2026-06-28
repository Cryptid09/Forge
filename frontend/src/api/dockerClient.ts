import { Job } from './apiClient';

const API_BASE_URL = 'http://localhost:8080/api/docker';

export interface DockerContainer {
    Id: string;
    Names: string[];
    Image: string;
    State: string;
    Status: string;
    Ports: Array<{ PrivatePort: number; PublicPort?: number; Type: string }>;
}

export interface DockerImage {
    Id: string;
    RepoTags: string[];
    Size: number;
    Created: number;
}

export const fetchContainers = async (): Promise<DockerContainer[]> => {
    // We execute the list-containers tool which returns a Job.
    // To simplify for the UI in this phase, we assume the job returns the JSON string in output,
    // but actually the execution is async. Let's add a small polling mechanism or just return the job.
    // For this demonstration, we'll fetch jobs and look for the output.
    // Note: A real implementation would have a dedicated synchronous endpoint or wait for the job.
    const res = await fetch(`${API_BASE_URL}/containers`);
    const job: Job = await res.json();
    return waitForJobOutput(job.id);
};

export const fetchImages = async (): Promise<DockerImage[]> => {
    const res = await fetch(`${API_BASE_URL}/images`);
    const job: Job = await res.json();
    return waitForJobOutput(job.id);
};

export const startContainer = async (containerId: string): Promise<Job> => {
    const res = await fetch(`${API_BASE_URL}/containers/start`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ containerId }),
    });
    return res.json();
};

export const stopContainer = async (containerId: string): Promise<Job> => {
    const res = await fetch(`${API_BASE_URL}/containers/stop`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ containerId }),
    });
    return res.json();
};

export const restartContainer = async (containerId: string): Promise<Job> => {
    const res = await fetch(`${API_BASE_URL}/containers/restart`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ containerId }),
    });
    return res.json();
};

export const removeContainer = async (containerId: string): Promise<Job> => {
    const res = await fetch(`${API_BASE_URL}/containers/${containerId}`, {
        method: 'DELETE',
    });
    return res.json();
};

export const pullImage = async (image: string): Promise<Job> => {
    const res = await fetch(`${API_BASE_URL}/images/pull`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ image }),
    });
    return res.json();
};

// Helper to poll for job completion and parse output (for listing ops)
async function waitForJobOutput(jobId: string, maxAttempts = 10): Promise<any> {
    for (let i = 0; i < maxAttempts; i++) {
        const res = await fetch(`http://localhost:8080/api/jobs/${jobId}`);
        const job: Job = await res.json();
        if (job.status === 'COMPLETED') {
            return JSON.parse(job.output || '[]');
        }
        if (job.status === 'FAILED') {
            throw new Error(job.errorMessage || 'Job failed');
        }
        await new Promise(resolve => setTimeout(resolve, 500));
    }
    throw new Error('Timeout waiting for job completion');
}
