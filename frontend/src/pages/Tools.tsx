import { useQuery } from '@tanstack/react-query';
import { fetchTools, executeTool } from '../api/apiClient';
import { Play, Search, Sparkles, Layers3 } from 'lucide-react';
import { useMemo, useState } from 'react';
import { FaToolbox } from 'react-icons/fa';
import { Loader } from '../components/ui/Loader';

export default function Tools() {
  const { data: tools = [], isLoading } = useQuery({
    queryKey: ['tools'],
    queryFn: fetchTools,
    staleTime: 5 * 60 * 1000,
    gcTime: 30 * 60 * 1000,
    refetchOnWindowFocus: false,
    refetchOnReconnect: false,
    refetchInterval: false,
  });
  const [executing, setExecuting] = useState<Record<string, boolean>>({});
  const [search, setSearch] = useState('');
  const [visibleCount, setVisibleCount] = useState(12);

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

  const filteredTools = useMemo(() => {
    const term = search.trim().toLowerCase();
    if (!term) return tools;
    return tools.filter(tool =>
      [tool.name, tool.description, tool.category, tool.version, tool.id]
        .filter(Boolean)
        .some(value => String(value).toLowerCase().includes(term)),
    );
  }, [search, tools]);

  const visibleTools = useMemo(
    () => filteredTools.slice(0, visibleCount),
    [filteredTools, visibleCount],
  );

  if (isLoading) return <Loader label="Loading tools..." />;

  return (
    <div className="space-y-6">
      <div className="rounded-2xl border border-slate-200 bg-linear-to-r from-white via-slate-50 to-white p-6 shadow-sm">
        <div className="flex flex-col gap-4 md:flex-row md:items-end md:justify-between">
          <div>
            <h2 className="text-2xl font-bold text-gray-900 flex items-center gap-2">
              <FaToolbox className="text-blue-600" />
              Available Tools
            </h2>
            <p className="mt-1 text-sm text-gray-500">Cached for faster browsing, with incremental rendering for large tool lists.</p>
          </div>
          <div className="flex items-center gap-2 rounded-xl border border-slate-200 bg-white px-3 py-2 shadow-sm md:w-90">
            <Search size={16} className="text-slate-400" />
            <input
              value={search}
              onChange={(e) => { setSearch(e.target.value); setVisibleCount(12); }}
              placeholder="Search tools, categories, versions..."
              className="w-full bg-transparent outline-none text-sm"
            />
          </div>
        </div>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
        {visibleTools.map(tool => (
          <div key={tool.id} className="bg-white p-6 rounded-xl shadow-sm border border-gray-100 flex flex-col h-full hover:shadow-md transition-shadow">
            <div className="flex items-start gap-3 flex-1">
              <div className="rounded-xl bg-blue-50 p-3 text-blue-600">
                <Layers3 size={18} />
              </div>
              <div className="min-w-0 flex-1">
                <h3 className="text-lg font-semibold text-gray-900 truncate">{tool.name}</h3>
                <span className="inline-flex items-center gap-1 px-2 py-1 bg-gray-100 text-gray-600 text-xs rounded mt-2">
                  <Sparkles size={12} /> {tool.category}
                </span>
                <p className="text-gray-500 mt-4 text-sm line-clamp-3">{tool.description}</p>
                <p className="text-gray-400 mt-2 text-xs">Version {tool.version}</p>
              </div>
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

      {visibleCount < filteredTools.length && (
        <div className="flex justify-center">
          <button
            onClick={() => setVisibleCount(count => count + 12)}
            className="rounded-lg border border-slate-200 bg-white px-4 py-2 text-sm font-medium text-slate-700 shadow-sm hover:bg-slate-50"
          >
            Load more tools
          </button>
        </div>
      )}
    </div>
  );
}
