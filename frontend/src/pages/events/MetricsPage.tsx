import React from 'react';
import { useQuery } from '@tanstack/react-query';
import apiClient from '../../api/apiClient';
import { BarChart3 } from 'lucide-react';

export const MetricsPage: React.FC = () => {
    const { data: metrics } = useQuery({
        queryKey: ['metrics'],
        queryFn: () => apiClient.get('/api/metrics')
    });

    return (
        <div className="flex flex-col h-full space-y-6">
            <h1 className="text-2xl font-bold text-gray-900 flex items-center space-x-2">
                <BarChart3 className="text-indigo-600" />
                <span>System Metrics</span>
            </h1>

            <div className="grid grid-cols-3 gap-6">
                <div className="bg-white shadow-sm border border-gray-200 rounded p-6 text-center">
                    <p className="text-gray-500 text-sm font-medium mb-2">Total Events Processed</p>
                    <p className="text-4xl font-bold text-indigo-600">{metrics?.length || 0}</p>
                </div>
                <div className="bg-white shadow-sm border border-gray-200 rounded p-6 text-center">
                    <p className="text-gray-500 text-sm font-medium mb-2">System Uptime</p>
                    <p className="text-4xl font-bold text-green-600">99.9%</p>
                </div>
                <div className="bg-white shadow-sm border border-gray-200 rounded p-6 text-center">
                    <p className="text-gray-500 text-sm font-medium mb-2">Build Success Rate</p>
                    <p className="text-4xl font-bold text-blue-600">100%</p>
                </div>
            </div>
        </div>
    );
};
