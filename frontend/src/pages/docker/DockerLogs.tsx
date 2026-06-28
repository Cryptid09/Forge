import { useEffect, useRef, useState } from 'react';
import { useParams, Link } from 'react-router-dom';
import { ArrowLeft, Play, Pause, Trash2 } from 'lucide-react';

export default function DockerLogs() {
  const { id } = useParams<{ id: string }>();
  const [logs, setLogs] = useState<string[]>([]);
  const [isPaused, setIsPaused] = useState(false);
  const wsRef = useRef<WebSocket | null>(null);
  const bottomRef = useRef<HTMLDivElement>(null);

  useEffect(() => {
    if (!id) return;
    
    const ws = new WebSocket('ws://localhost:8080/ws/docker/logs');
    wsRef.current = ws;

    ws.onopen = () => {
      ws.send(id);
    };

    ws.onmessage = (event) => {
      if (!isPaused) {
        setLogs(prev => [...prev, event.data]);
      }
    };

    return () => {
      ws.close();
    };
  }, [id, isPaused]);

  useEffect(() => {
    if (!isPaused) {
      bottomRef.current?.scrollIntoView({ behavior: 'smooth' });
    }
  }, [logs, isPaused]);

  return (
    <div className="h-full flex flex-col space-y-4">
      <div className="flex items-center justify-between">
        <div className="flex items-center gap-4">
          <Link to="/docker/containers" className="text-gray-500 hover:text-gray-900">
            <ArrowLeft size={20} />
          </Link>
          <h2 className="text-xl font-bold text-gray-900">Container Logs</h2>
          <span className="text-sm font-mono text-gray-500 bg-gray-100 px-2 py-1 rounded">{id?.substring(0, 12)}</span>
        </div>
        
        <div className="flex items-center gap-2">
          <button 
            onClick={() => setIsPaused(!isPaused)} 
            className="flex items-center gap-2 px-3 py-1.5 text-sm bg-gray-100 hover:bg-gray-200 text-gray-700 rounded transition-colors"
          >
            {isPaused ? <><Play size={16} /> Resume</> : <><Pause size={16} /> Pause</>}
          </button>
          <button 
            onClick={() => setLogs([])} 
            className="flex items-center gap-2 px-3 py-1.5 text-sm bg-gray-100 hover:bg-gray-200 text-gray-700 rounded transition-colors"
          >
            <Trash2 size={16} /> Clear
          </button>
        </div>
      </div>

      <div className="flex-1 bg-[#1e1e1e] rounded-xl shadow-inner border border-gray-800 overflow-hidden flex flex-col relative h-[600px]">
        <div className="absolute top-0 w-full h-8 bg-gray-900 border-b border-gray-800 flex items-center px-4">
          <div className="flex gap-2">
            <div className="w-3 h-3 rounded-full bg-red-500"></div>
            <div className="w-3 h-3 rounded-full bg-yellow-500"></div>
            <div className="w-3 h-3 rounded-full bg-green-500"></div>
          </div>
          <span className="ml-4 text-xs text-gray-400 font-mono">bash - {id?.substring(0, 12)}</span>
        </div>
        
        <div className="flex-1 overflow-auto p-4 pt-12 font-mono text-sm text-gray-300">
          {logs.map((log, i) => (
            <div key={i} className="mb-1 leading-relaxed whitespace-pre-wrap break-all hover:bg-gray-800/50 px-2 -mx-2 rounded">
              {log}
            </div>
          ))}
          {logs.length === 0 && <div className="text-gray-500 italic">Waiting for logs...</div>}
          <div ref={bottomRef} />
        </div>
      </div>
    </div>
  );
}
