import { useQuery } from '@tanstack/react-query';
import { fetchTools, executeTool } from '../api/apiClient';
import { Play } from 'lucide-react';
import { useState } from 'react';

export default function Tools() {
  const { data: tools = [], isLoading } = useQuery({ queryKey: ['tools'], queryFn: fetchTools });
  const [executing, setExecuting] = useState<Record<string, boolean>>({});

  const handleExecute = async (toolId: string) => {
    try {
      setExecuting(prev => ({ ...prev, [toolId]: true }));
      let params: Record<string, string> = {};
      if (toolId === 'echo-tool') {
        params = { message: 'Hello from UI!' };
      } else if (toolId === 'sleep-tool') {
        params = { durationMs: '5000' };
      }
      await executeTool(toolId, params);
      alert('Job started successfully!');
    } catch (e) {
      alert('Failed to start job');
    } finally {
      setExecuting(prev => ({ ...prev, [toolId]: false }));
    }
  };

  if (isLoading) return <div>Loading tools...</div>;

  return (
    <div className="space-y-6">
      <h2 className="text-2xl font-bold text-gray-900">Available Tools</h2>
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
        {tools.map(tool => (
          <div key={tool.id} className="bg-white p-6 rounded-xl shadow-sm border border-gray-100 flex flex-col h-full">
            <div className="flex-1">
              <h3 className="text-lg font-semibold text-gray-900">{tool.name}</h3>
              <span className="inline-block px-2 py-1 bg-gray-100 text-gray-600 text-xs rounded mt-2">{tool.category}</span>
              <p className="text-gray-500 mt-4 text-sm">{tool.description}</p>
              <p className="text-gray-400 mt-2 text-xs">Version {tool.version}</p>
            </div>
            <div className="mt-6">
              <button
                onClick={() => handleExecute(tool.id)}
                disabled={executing[tool.id]}
                className="w-full flex items-center justify-center gap-2 bg-blue-600 hover:bg-blue-700 disabled:bg-blue-400 text-white py-2 px-4 rounded-lg transition-colors"
              >
                <Play size={16} />
                {executing[tool.id] ? 'Executing...' : 'Execute Tool'}
              </button>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
}
