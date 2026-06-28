import React from 'react';
import { useQuery } from '@tanstack/react-query';
import apiClient from '../../api/apiClient';
import { ShieldAlert } from 'lucide-react';

export const AuditPage: React.FC = () => {
    const { data: logs } = useQuery({
        queryKey: ['auditLogs'],
        queryFn: () => apiClient.get('/api/audit')
    });

    return (
        <div className="flex flex-col h-full space-y-6">
            <h1 className="text-2xl font-bold text-gray-900 flex items-center space-x-2">
                <ShieldAlert className="text-red-600" />
                <span>Audit Logs (Immutable)</span>
            </h1>

            <div className="bg-white shadow-sm border border-gray-200 rounded overflow-hidden">
                <table className="min-w-full divide-y divide-gray-200">
                    <thead className="bg-gray-50">
                        <tr>
                            <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Event ID</th>
                            <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Type</th>
                            <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Source</th>
                            <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Timestamp</th>
                        </tr>
                    </thead>
                    <tbody className="bg-white divide-y divide-gray-200">
                        {logs?.map((log: any) => (
                            <tr key={log.id}>
                                <td className="px-6 py-4 whitespace-nowrap text-sm font-mono text-gray-500">{log.id.substring(0, 8)}...</td>
                                <td className="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">{log.eventType}</td>
                                <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">{log.source}</td>
                                <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">{new Date(log.timestamp).toLocaleString()}</td>
                            </tr>
                        ))}
                    </tbody>
                </table>
            </div>
        </div>
    );
};
