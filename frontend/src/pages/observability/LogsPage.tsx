import React, { useState, useEffect, useRef } from 'react';
import { Play, Square, Download, Search } from 'lucide-react';
import { FiFileText } from 'react-icons/fi';

export const LogsPage: React.FC = () => {
    const [source, setSource] = useState('');
    const [logs, setLogs] = useState<{ id: number, text: string }[]>([]);
    const [streaming, setStreaming] = useState(false);
    const [autoScroll, setAutoScroll] = useState(true);
    const [search, setSearch] = useState('');
    const wsRef = useRef<WebSocket | null>(null);
    const bottomRef = useRef<HTMLDivElement>(null);
    const counter = useRef(0);

    const toggleStream = () => {
        if (streaming) {
            if (wsRef.current) wsRef.current.close();
            setStreaming(false);
        } else {
            if (!source.trim()) return;
            setLogs([]);
            const ws = new WebSocket(`ws://${window.location.host}/ws/logs/stream?source=${source}`);
            ws.onmessage = (e) => {
                const msg = JSON.parse(e.data);
                if (msg.type === 'log') {
                    setLogs(prev => [...prev.slice(-1000), { id: counter.current++, text: msg.data }]);
                } else if (msg.type === 'done' || msg.type === 'error') {
                    setStreaming(false);
                }
            };
            ws.onclose = () => setStreaming(false);
            wsRef.current = ws;
            setStreaming(true);
        }
    };

    useEffect(() => {
        if (autoScroll) {
            bottomRef.current?.scrollIntoView({ behavior: 'smooth' });
        }
    }, [logs, autoScroll]);

    const handleDownload = () => {
        const text = logs.map(l => l.text).join('\n');
        const blob = new Blob([text], { type: 'text/plain' });
        const url = URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.href = url;
        a.download = `logs-${source}-${new Date().toISOString()}.log`;
        a.click();
        URL.revokeObjectURL(url);
    };

    const filteredLogs = logs.filter(l => l.text.toLowerCase().includes(search.toLowerCase()));

    return (
        <div className="flex flex-col h-full p-6">
            <div className="flex justify-between items-center mb-6">
                <h1 className="text-2xl font-bold text-gray-900 flex items-center gap-2"><FiFileText className="text-indigo-600" /> <span>Live Log Streaming</span></h1>
                <div className="flex space-x-4">
                    <input
                        type="text"
                        placeholder="Container ID or name..."
                        value={source}
                        onChange={(e) => setSource(e.target.value)}
                        disabled={streaming}
                        className="border border-gray-300 rounded px-3 py-2 w-64"
                    />
                    <button 
                        onClick={toggleStream} 
                        className={`px-4 py-2 flex items-center space-x-2 rounded text-white ${streaming ? 'bg-red-600 hover:bg-red-700' : 'bg-indigo-600 hover:bg-indigo-700'}`}
                    >
                        {streaming ? <><Square size={18}/> <span>Stop</span></> : <><Play size={18}/> <span>Stream</span></>}
                    </button>
                    <button onClick={handleDownload} disabled={logs.length === 0} className="px-4 py-2 border border-gray-300 rounded hover:bg-gray-50 flex items-center space-x-2 disabled:opacity-50">
                        <Download size={18} /> <span>Download</span>
                    </button>
                </div>
            </div>

            <div className="flex items-center space-x-4 mb-4 bg-white p-3 border border-gray-200 rounded">
                <div className="flex items-center flex-1 bg-gray-50 px-3 py-1 rounded border border-gray-200">
                    <Search size={16} className="text-gray-400 mr-2" />
                    <input 
                        type="text" 
                        placeholder="Filter logs..." 
                        value={search} 
                        onChange={(e) => setSearch(e.target.value)} 
                        className="bg-transparent border-none outline-none flex-1 py-1"
                    />
                </div>
                <label className="flex items-center space-x-2 text-sm text-gray-700 cursor-pointer">
                    <input type="checkbox" checked={autoScroll} onChange={(e) => setAutoScroll(e.target.checked)} className="rounded text-indigo-600 focus:ring-indigo-500" />
                    <span>Auto-scroll</span>
                </label>
            </div>

            <div className="flex-1 bg-gray-900 text-gray-100 font-mono text-sm p-4 rounded overflow-y-auto shadow-inner">
                {filteredLogs.length === 0 ? (
                    <div className="text-gray-500 flex justify-center items-center h-full">
                        {streaming ? 'Waiting for logs...' : 'No logs to display.'}
                    </div>
                ) : (
                    <div className="whitespace-pre-wrap">
                        {filteredLogs.map(log => (
                            <div key={log.id} className="leading-relaxed hover:bg-gray-800 px-1 rounded">
                                {log.text}
                            </div>
                        ))}
                        <div ref={bottomRef} />
                    </div>
                )}
            </div>
        </div>
    );
};
