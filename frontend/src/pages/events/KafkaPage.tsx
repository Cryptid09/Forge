import React from 'react';
import { useQuery } from '@tanstack/react-query';
import apiClient from '../../api/apiClient';
import { Database, Server } from 'lucide-react';
import { SiApachekafka } from 'react-icons/si';

export const KafkaPage: React.FC = () => {
    const { data: topics } = useQuery({
        queryKey: ['kafkaTopics'],
        queryFn: () => apiClient.get('/api/kafka/topics')
    });

    const { data: consumers } = useQuery({
        queryKey: ['kafkaConsumers'],
        queryFn: () => apiClient.get('/api/kafka/consumers')
    });

    return (
        <div className="flex h-full flex-col space-y-6">
            <div className="rounded-2xl border border-slate-200 bg-linear-to-r from-purple-50 via-indigo-50 to-white p-6 shadow-sm">
                <div className="flex flex-col gap-4 md:flex-row md:items-center md:justify-between">
                    <h1 className="flex items-center space-x-3 text-2xl font-bold text-slate-900">
                        <SiApachekafka className="h-8 w-8 text-[#231F20]" />
                        <span>Kafka Event Streaming</span>
                    </h1>
                    <a href="http://localhost:8081" target="_blank" rel="noreferrer" className="inline-flex items-center rounded-lg bg-indigo-600 px-4 py-2 text-sm font-medium text-white shadow-sm transition-colors hover:bg-indigo-700">
                        Open Kafka UI
                    </a>
                </div>
            </div>

            <div className="grid grid-cols-2 gap-6">
                <div className="bg-white shadow-sm border border-gray-200 rounded p-6">
                    <h2 className="text-lg font-bold text-gray-800 mb-4 flex items-center"><Database size={20} className="mr-2"/> Topics</h2>
                    <ul className="space-y-3">
                        {topics?.map((t: any) => (
                            <li key={t.name} className="flex justify-between items-center p-3 bg-gray-50 rounded border border-gray-100">
                                <div>
                                    <span className="font-medium text-gray-800">{t.name}</span>
                                    <p className="text-xs text-gray-500">{t.partitions} partitions</p>
                                </div>
                                <span className="bg-indigo-100 text-indigo-800 px-2 py-1 rounded text-xs font-semibold">{t.messages} messages</span>
                            </li>
                        ))}
                    </ul>
                </div>

                <div className="bg-white shadow-sm border border-gray-200 rounded p-6">
                    <h2 className="text-lg font-bold text-gray-800 mb-4 flex items-center"><Server size={20} className="mr-2"/> Consumers</h2>
                    <ul className="space-y-3">
                        {consumers?.map((c: any) => (
                            <li key={c.groupId} className="flex justify-between items-center p-3 bg-gray-50 rounded border border-gray-100">
                                <div>
                                    <span className="font-medium text-gray-800">{c.groupId}</span>
                                    <p className="text-xs text-gray-500">Lag: {c.lag}</p>
                                </div>
                                <span className="bg-green-100 text-green-800 px-2 py-1 rounded text-xs font-semibold">{c.state}</span>
                            </li>
                        ))}
                    </ul>
                </div>
            </div>
        </div>
    );
};
