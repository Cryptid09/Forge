import apiClient from './apiClient';

export interface TerminalSession {
    sessionId: string;
    status: string;
    workingDirectory: string;
    createdAt: string;
    lastActivity: string;
    commandHistory: string[];
}

export const terminalClient = {
    createSession: async (workingDirectory?: string): Promise<TerminalSession> => {
        return apiClient.post('/api/terminal/session', workingDirectory ? { workingDirectory } : {});
    },

    listSessions: async (): Promise<TerminalSession[]> => {
        return apiClient.get('/api/terminal/session');
    },

    getSession: async (id: string): Promise<TerminalSession> => {
        return apiClient.get(`/api/terminal/session/${id}`);
    },

    closeSession: async (id: string): Promise<void> => {
        return apiClient.delete(`/api/terminal/session/${id}`);
    },

    executeCommand: async (id: string, command: string): Promise<{ jobId: string, status: string, output: string }> => {
        return apiClient.post(`/api/terminal/session/${id}/command`, { command });
    },
};
