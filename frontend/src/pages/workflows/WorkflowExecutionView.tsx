import { useEffect, useState } from 'react';
import { useParams, Link } from 'react-router-dom';
import { fetchWorkflowExecution, WorkflowExecution } from '../../api/workflowClient';
import { ArrowLeft, CheckCircle2, XCircle, Clock, CircleDashed } from 'lucide-react';
import { useQuery, useQueryClient } from '@tanstack/react-query';

export default function WorkflowExecutionView() {
  const { id } = useParams<{ id: string }>();
  const queryClient = useQueryClient();

  const { data: execution, isLoading } = useQuery({
    queryKey: ['workflow-execution', id],
    queryFn: () => fetchWorkflowExecution(id!),
    enabled: !!id
  });

  useEffect(() => {
    if (!id) return;
    const ws = new WebSocket('ws://localhost:8080/ws/workflow/progress');
    
    ws.onmessage = (event) => {
      const data = JSON.parse(event.data);
      if (data.eventType.startsWith('Workflow')) {
        queryClient.invalidateQueries({ queryKey: ['workflow-execution', id] });
      }
    };
    return () => ws.close();
  }, [id, queryClient]);

  if (isLoading || !execution) return <div>Loading execution...</div>;

  const StatusIcon = ({ status }: { status: string }) => {
    switch (status) {
      case 'COMPLETED': return <CheckCircle2 className="text-green-500" size={20} />;
      case 'FAILED': return <XCircle className="text-red-500" size={20} />;
      case 'RUNNING': return <CircleDashed className="text-blue-500 animate-spin" size={20} />;
      default: return <Clock className="text-gray-400" size={20} />;
    }
  };

  return (
    <div className="max-w-4xl mx-auto space-y-6">
      <div className="flex items-center gap-4">
        <Link to="/workflows" className="text-gray-500 hover:text-gray-900">
          <ArrowLeft size={20} />
        </Link>
        <h2 className="text-2xl font-bold text-gray-900 flex items-center gap-3">
          {execution.workflowName}
          <span className={`text-sm px-2.5 py-1 rounded-full font-medium ${
            execution.status === 'COMPLETED' ? 'bg-green-100 text-green-800' :
            execution.status === 'FAILED' ? 'bg-red-100 text-red-800' :
            execution.status === 'RUNNING' ? 'bg-blue-100 text-blue-800' :
            'bg-gray-100 text-gray-800'
          }`}>
            {execution.status}
          </span>
        </h2>
      </div>

      <div className="bg-white rounded-xl shadow-sm border border-gray-200 overflow-hidden">
        <div className="px-6 py-4 border-b border-gray-100 bg-gray-50/50">
          <h3 className="font-semibold text-gray-700">Execution Steps</h3>
        </div>
        <div className="divide-y divide-gray-100">
          {execution.stepExecutions?.map((step) => (
            <div key={step.id} className="p-6 flex items-start gap-4">
              <div className="mt-1">
                <StatusIcon status={step.status} />
              </div>
              <div className="flex-1">
                <div className="flex items-center justify-between">
                  <h4 className="font-medium text-gray-900">{step.stepOrder}. {step.stepName}</h4>
                  <span className="text-xs font-mono text-gray-500 bg-gray-100 px-2 py-1 rounded">
                    {step.toolId}
                  </span>
                </div>
                {step.errorMessage && (
                  <div className="mt-2 text-sm text-red-600 bg-red-50 p-3 rounded-md border border-red-100">
                    {step.errorMessage}
                  </div>
                )}
                <div className="mt-2 flex gap-4 text-xs text-gray-500">
                  {step.startedAt && <span>Started: {new Date(step.startedAt).toLocaleTimeString()}</span>}
                  {step.attemptNumber > 1 && <span className="text-orange-600 font-medium">Attempts: {step.attemptNumber}</span>}
                </div>
              </div>
            </div>
          ))}
          {!execution.stepExecutions?.length && (
            <div className="p-8 text-center text-gray-500">No steps executed yet.</div>
          )}
        </div>
      </div>
    </div>
  );
}
