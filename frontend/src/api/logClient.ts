import apiClient from './apiClient';

export interface LogEntry {
    lineNumber: number;
    level: string;
    message: string;
    timestamp: string;
}

export interface AnalysisResult {
    summary: string;
    severity: string;
    totalLines: number;
    errorCount: number;
    warnCount: number;
    findings: {
        patternName: string;
        severity: string;
        lineNumber: number;
        excerpt: string;
        occurrences: number;
    }[];
    suggestedRootCause: string;
    timeline: {
        lineNumber: number;
        timestamp: string;
        level: string;
        message: string;
    }[];
    analyzedAt: string;
}

export const logClient = {
    parseAndFilter: async (logs: string, search?: string, levels?: string, caseSensitive?: boolean, regex?: boolean): Promise<LogEntry[]> => {
        const params = new URLSearchParams();
        if (search) params.append('search', search);
        if (levels) params.append('levels', levels);
        if (caseSensitive) params.append('caseSensitive', 'true');
        if (regex) params.append('regex', 'true');
        
        return apiClient.post(`/api/logs?${params.toString()}`, { logs });
    },

    analyze: async (logs: string, source?: string): Promise<{ jobId: string, status: string, analysis: AnalysisResult }> => {
        return apiClient.post('/api/logs/analyze', { logs, source });
    },
};
