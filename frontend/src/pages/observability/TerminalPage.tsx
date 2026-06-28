import React, { useState, useEffect, useRef } from 'react';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { terminalClient } from '../../api/terminalClient';
import { Play, Square, Terminal as TerminalIcon } from 'lucide-react';

export const TerminalPage: React.FC = () => {
    const queryClient = useQueryClient();
    const [sessionId, setSessionId] = useState<string | null>(null);
    const [command, setCommand] = useState('');
    const [output, setOutput] = useState<{ type: string, data: string }[]>([]);
    const [cwd, setCwd] = useState<string>('~');
    const wsRef = useRef<WebSocket | null>(null);
    const bottomRef = useRef<HTMLDivElement>(null);

    const { data: sessions, refetch } = useQuery({
        queryKey: ['terminalSessions'],
        queryFn: terminalClient.listSessions
    });

    const createMutation = useMutation({
        mutationFn: () => terminalClient.createSession(),
        onSuccess: (session) => {
            queryClient.invalidateQueries({ queryKey: ['terminalSessions'] });
            connectWs(session.sessionId);
        }
    });

    const closeMutation = useMutation({
        mutationFn: (id: string) => terminalClient.closeSession(id),
        onSuccess: () => {
            queryClient.invalidateQueries({ queryKey: ['terminalSessions'] });
            if (wsRef.current) wsRef.current.close();
            setSessionId(null);
            setOutput([]);
        }
    });

    const connectWs = (id: string) => {
        if (wsRef.current) wsRef.current.close();
        setSessionId(id);
        setOutput([]);
        const wsUrl = `ws://${window.location.host}/ws/terminal?sessionId=${id}`;
        const ws = new WebSocket(wsUrl);
        ws.onmessage = (event) => {
            const msg = JSON.parse(event.data);
            if (msg.type === 'connected' || msg.type === 'cwd' || msg.type === 'done') {
                if (msg.cwd) setCwd(msg.cwd);
            }
            if (msg.type === 'output' || msg.type === 'error') {
                setOutput(prev => [...prev, msg]);
            }
        };
        ws.onclose = () => {
            setOutput(prev => [...prev, { type: 'error', data: 'Session disconnected.\n' }]);
        };
        wsRef.current = ws;
    };

    useEffect(() => {
        bottomRef.current?.scrollIntoView({ behavior: 'smooth' });
    }, [output]);

    const handleExecute = (e: React.FormEvent) => {
        e.preventDefault();
        if (!command.trim() || !wsRef.current || wsRef.current.readyState !== WebSocket.OPEN) return;
        
        setOutput(prev => [...prev, { type: 'input', data: `${cwd} $ ${command}\n` }]);
        wsRef.current.send(JSON.stringify({ command }));
        setCommand('');
    };

    return (
        <div className="flex flex-col h-full bg-gray-900 text-gray-100 p-4 font-mono">
            <div className="flex justify-between items-center mb-4">
                <div className="flex items-center space-x-2">
                    <TerminalIcon className="text-green-400" />
                    <h1 className="text-xl font-bold">Interactive Terminal</h1>
                </div>
                <div className="space-x-2">
                    {sessions?.map(s => (
                        <button key={s.sessionId} onClick={() => connectWs(s.sessionId)} className={`px-3 py-1 rounded text-sm ${sessionId === s.sessionId ? 'bg-green-600' : 'bg-gray-700 hover:bg-gray-600'}`}>
                            {s.sessionId.substring(0, 8)}
                        </button>
                    ))}
                    <button onClick={() => createMutation.mutate()} className="bg-blue-600 hover:bg-blue-700 px-3 py-1 rounded text-sm flex items-center space-x-1">
                        <Play size={16} /> <span>New Session</span>
                    </button>
                    {sessionId && (
                        <button onClick={() => closeMutation.mutate(sessionId)} className="bg-red-600 hover:bg-red-700 px-3 py-1 rounded text-sm flex items-center space-x-1">
                            <Square size={16} /> <span>Close</span>
                        </button>
                    )}
                </div>
            </div>

            <div className="flex-1 bg-black rounded p-4 overflow-y-auto border border-gray-700 shadow-inner">
                {sessionId ? (
                    <>
                        <div className="whitespace-pre-wrap font-mono text-sm leading-relaxed text-gray-300">
                            {output.map((msg, idx) => (
                                <span key={idx} className={msg.type === 'error' ? 'text-red-400' : msg.type === 'input' ? 'text-green-400 font-bold' : ''}>
                                    {msg.data}
                                </span>
                            ))}
                            <div ref={bottomRef} />
                        </div>
                    </>
                ) : (
                    <div className="flex items-center justify-center h-full text-gray-500">
                        Create or select a session to begin.
                    </div>
                )}
            </div>

            {sessionId && (
                <form onSubmit={handleExecute} className="mt-4 flex space-x-2">
                    <div className="bg-black text-green-400 px-3 py-2 rounded-l border border-r-0 border-gray-700 flex items-center">
                        {cwd} $
                    </div>
                    <input
                        type="text"
                        value={command}
                        onChange={(e) => setCommand(e.target.value)}
                        className="flex-1 bg-black text-white px-3 py-2 border border-gray-700 focus:outline-none focus:border-green-500 rounded-r"
                        placeholder="Enter command..."
                        autoFocus
                    />
                </form>
            )}
        </div>
    );
};
