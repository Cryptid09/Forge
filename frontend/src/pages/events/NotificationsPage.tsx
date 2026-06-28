import React from 'react';
import { useQuery } from '@tanstack/react-query';
import apiClient from '../../api/apiClient';
import { Bell } from 'lucide-react';

export const NotificationsPage: React.FC = () => {
    const { data: notifications } = useQuery({
        queryKey: ['notifications'],
        queryFn: () => apiClient.get('/api/notifications')
    });

    return (
        <div className="flex flex-col h-full space-y-6">
            <h1 className="text-2xl font-bold text-gray-900 flex items-center space-x-2">
                <Bell className="text-indigo-600" />
                <span>Notifications</span>
            </h1>

            <div className="space-y-4">
                {notifications?.map((n: any) => (
                    <div key={n.id} className={`p-4 rounded border-l-4 ${n.severity === 'ERROR' ? 'border-red-500 bg-red-50' : 'border-blue-500 bg-blue-50'}`}>
                        <div className="flex justify-between items-start">
                            <div>
                                <h3 className={`font-bold ${n.severity === 'ERROR' ? 'text-red-800' : 'text-blue-800'}`}>{n.message}</h3>
                                <p className="text-sm text-gray-600 mt-1">Source: {n.source}</p>
                            </div>
                            <span className="text-xs text-gray-500">{new Date(n.timestamp).toLocaleString()}</span>
                        </div>
                    </div>
                ))}
                {notifications?.length === 0 && <p className="text-gray-500">No notifications.</p>}
            </div>
        </div>
    );
};
