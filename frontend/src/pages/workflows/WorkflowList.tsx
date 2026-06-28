import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { fetchWorkflows, deleteWorkflow, executeWorkflow, importYamlWorkflow } from '../../api/workflowClient';
import { Link, useNavigate } from 'react-router-dom';
import { Play, Plus, Trash2, FileText, Activity } from 'lucide-react';
import { useState } from 'react';

export default function WorkflowList() {
  const queryClient = useQueryClient();
  const navigate = useNavigate();
  const { data: workflows = [], isLoading } = useQuery({ queryKey: ['workflows'], queryFn: fetchWorkflows });

  const [showYamlModal, setShowYamlModal] = useState(false);
  const [yamlText, setYamlText] = useState('');

  const delMut = useMutation({
    mutationFn: deleteWorkflow,
    onSuccess: () => queryClient.invalidateQueries({ queryKey: ['workflows'] })
  });

  const execMut = useMutation({
    mutationFn: executeWorkflow,
    onSuccess: (execution) => {
      navigate(`/workflows/executions/${execution.id}`);
    }
  });

  const importMut = useMutation({
    mutationFn: importYamlWorkflow,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['workflows'] });
      setShowYamlModal(false);
      setYamlText('');
    },
    onError: (err: any) => alert(err.message)
  });

  if (isLoading) return <div>Loading workflows...</div>;

  return (
    <div className="space-y-6">
      <div className="flex items-center justify-between">
        <h2 className="text-2xl font-bold text-gray-900 flex items-center gap-2">
          <Activity className="text-blue-600" /> Workflows
        </h2>
        <div className="flex gap-2">
          <button 
            onClick={() => setShowYamlModal(true)}
            className="flex items-center gap-2 px-4 py-2 bg-gray-100 text-gray-700 rounded-md hover:bg-gray-200"
          >
            <FileText size={18} /> Import YAML
          </button>
          <Link 
            to="/workflows/new" 
            className="flex items-center gap-2 px-4 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700"
          >
            <Plus size={18} /> New Workflow
          </Link>
        </div>
      </div>

      <div className="bg-white rounded-xl shadow-sm border border-gray-100 overflow-hidden">
        <table className="min-w-full divide-y divide-gray-200">
          <thead className="bg-gray-50">
            <tr>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Name</th>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Steps</th>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Version</th>
              <th className="px-6 py-3 text-right text-xs font-medium text-gray-500 uppercase">Actions</th>
            </tr>
          </thead>
          <tbody className="bg-white divide-y divide-gray-200">
            {workflows.map(w => (
              <tr key={w.id}>
                <td className="px-6 py-4 whitespace-nowrap">
                  <div className="font-medium text-gray-900">{w.name}</div>
                  <div className="text-xs text-gray-500">{w.description}</div>
                </td>
                <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">{w.steps?.length || 0}</td>
                <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">v{w.version}</td>
                <td className="px-6 py-4 whitespace-nowrap text-right text-sm font-medium space-x-3">
                  <button onClick={() => execMut.mutate(w.id)} className="text-green-600 hover:text-green-900" title="Execute">
                    <Play size={18} />
                  </button>
                  <button onClick={() => delMut.mutate(w.id)} className="text-red-600 hover:text-red-900" title="Delete">
                    <Trash2 size={18} />
                  </button>
                </td>
              </tr>
            ))}
            {workflows.length === 0 && (
              <tr><td colSpan={4} className="px-6 py-10 text-center text-gray-500">No workflows found.</td></tr>
            )}
          </tbody>
        </table>
      </div>

      {showYamlModal && (
        <div className="fixed inset-0 bg-black/50 flex items-center justify-center z-50">
          <div className="bg-white p-6 rounded-xl shadow-xl w-[600px] flex flex-col h-[500px]">
            <h3 className="text-lg font-bold mb-4">Import YAML Workflow</h3>
            <textarea
              className="flex-1 border rounded-md p-3 font-mono text-sm mb-4 focus:ring-2 focus:ring-blue-500 outline-none resize-none"
              placeholder="name: My Workflow&#10;steps:&#10;  - tool: echo-tool"
              value={yamlText}
              onChange={e => setYamlText(e.target.value)}
            />
            <div className="flex justify-end gap-2">
              <button onClick={() => setShowYamlModal(false)} className="px-4 py-2 text-gray-600 hover:bg-gray-100 rounded-md">Cancel</button>
              <button onClick={() => importMut.mutate(yamlText)} disabled={!yamlText || importMut.isPending} className="px-4 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700 disabled:opacity-50">Import</button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}
