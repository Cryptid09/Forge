import { useQuery } from '@tanstack/react-query';
import { fetchJobs, fetchTools } from '../api/apiClient';
import { Activity, CheckCircle, Clock, XCircle } from 'lucide-react';

export default function Dashboard() {
  const { data: jobs = [] } = useQuery({ queryKey: ['jobs'], queryFn: fetchJobs, refetchInterval: 5000 });
  const { data: tools = [] } = useQuery({ queryKey: ['tools'], queryFn: fetchTools });

  const stats = {
    totalTools: tools.length,
    totalJobs: jobs.length,
    completedJobs: jobs.filter(j => j.status === 'COMPLETED').length,
    failedJobs: jobs.filter(j => j.status === 'FAILED').length,
    runningJobs: jobs.filter(j => j.status === 'RUNNING').length,
  };

  return (
    <div className="space-y-6">
      <h2 className="text-2xl font-bold text-gray-900">Dashboard</h2>
      
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
        <div className="bg-white p-6 rounded-xl shadow-sm border border-gray-100 flex items-center gap-4">
          <div className="bg-blue-100 p-3 rounded-lg text-blue-600"><Activity size={24} /></div>
          <div>
            <p className="text-sm font-medium text-gray-500">Total Jobs</p>
            <p className="text-2xl font-semibold text-gray-900">{stats.totalJobs}</p>
          </div>
        </div>
        
        <div className="bg-white p-6 rounded-xl shadow-sm border border-gray-100 flex items-center gap-4">
          <div className="bg-green-100 p-3 rounded-lg text-green-600"><CheckCircle size={24} /></div>
          <div>
            <p className="text-sm font-medium text-gray-500">Completed</p>
            <p className="text-2xl font-semibold text-gray-900">{stats.completedJobs}</p>
          </div>
        </div>
        
        <div className="bg-white p-6 rounded-xl shadow-sm border border-gray-100 flex items-center gap-4">
          <div className="bg-yellow-100 p-3 rounded-lg text-yellow-600"><Clock size={24} /></div>
          <div>
            <p className="text-sm font-medium text-gray-500">Running</p>
            <p className="text-2xl font-semibold text-gray-900">{stats.runningJobs}</p>
          </div>
        </div>
        
        <div className="bg-white p-6 rounded-xl shadow-sm border border-gray-100 flex items-center gap-4">
          <div className="bg-red-100 p-3 rounded-lg text-red-600"><XCircle size={24} /></div>
          <div>
            <p className="text-sm font-medium text-gray-500">Failed</p>
            <p className="text-2xl font-semibold text-gray-900">{stats.failedJobs}</p>
          </div>
        </div>
      </div>
    </div>
  );
}
