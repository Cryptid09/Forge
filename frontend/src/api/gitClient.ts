import apiClient from './apiClient';

export interface GitRepository {
    id: string;
    name: string;
    remoteUrl: string;
    localPath: string;
    createdAt: string;
}

export const gitClient = {
    listRepositories: async (): Promise<GitRepository[]> => {
        return apiClient.get('/api/git/repositories');
    },

    registerRepository: async (name: string, remoteUrl: string, localPath: string): Promise<GitRepository> => {
        return apiClient.post('/api/git/repositories', { name, remoteUrl, localPath });
    },

    unregisterRepository: async (id: string): Promise<void> => {
        return apiClient.delete(`/api/git/repositories/${id}`);
    },

    cloneRepository: async (remoteUrl: string, localPath: string): Promise<{ jobId: string, status: string }> => {
        return apiClient.post('/api/git/clone', { remoteUrl, localPath });
    },

    pullRepository: async (repositoryId: string): Promise<{ jobId: string, status: string }> => {
        return apiClient.post('/api/git/pull', { repositoryId });
    },

    checkoutBranch: async (repositoryId: string, branch: string): Promise<{ jobId: string, status: string }> => {
        return apiClient.post('/api/git/checkout', { repositoryId, branch });
    }
};
