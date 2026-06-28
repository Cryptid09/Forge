import apiClient from './apiClient';

export interface BuildArtifact {
    id: string;
    repositoryId: string;
    name: string;
    type: string;
    sizeBytes: number;
    localPath: string;
    buildTime: string;
}

export const buildClient = {
    runBuild: async (repositoryId: string, buildTool: string, tasks: string): Promise<{ jobId: string, status: string }> => {
        return apiClient.post('/api/build/run', { repositoryId, buildTool, tasks });
    },

    listArtifacts: async (repositoryId?: string): Promise<BuildArtifact[]> => {
        return apiClient.get(repositoryId ? `/api/build/artifacts?repositoryId=${repositoryId}` : '/api/build/artifacts');
    }
};
