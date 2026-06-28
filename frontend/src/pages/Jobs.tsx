import { useQuery } from '@tanstack/react-query';
import { fetchJobs } from '../api/apiClient';

export default function Jobs() {
  const { data: jobs = [], isLoading } = useQuery({ queryKey: ['jobs'], queryFn: fetchJobs, refetchInterval: 3000 });

  if (isLoading) return <div>Loading jobs...</div>;

  return (
    <div className="space-y-6">
      <h2 className="text-2xl font-bold text-gray-900">Job History</h2>
      <div className="bg-white rounded-xl shadow-sm border border-gray-100 overflow-hidden">
        <table className="min-w-full divide-y divide-gray-200">
          <thead className="bg-gray-50">
            <tr>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Job ID</th>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Tool</th>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Status</th>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Start Time</th>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Duration</th>
            </tr>
          </thead>
          <tbody className="bg-white divide-y divide-gray-200">
            {jobs.map(job => (
              <tr key={job.id}>
                <td className="px-6 py-4 whitespace-nowrap text-sm font-mono text-gray-900 truncate max-w-[200px]" title={job.id}>{job.id}</td>
                <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">{job.toolId}</td>
                <td className="px-6 py-4 whitespace-nowrap">
                  <span className={`px-2 inline-flex text-xs leading-5 font-semibold rounded-full 
                    ${job.status === 'COMPLETED' ? 'bg-green-100 text-green-800' : 
                      job.status === 'FAILED' ? 'bg-red-100 text-red-800' : 
                      job.status === 'RUNNING' ? 'bg-yellow-100 text-yellow-800' : 'bg-gray-100 text-gray-800'}`}>
                    {job.status}
                  </span>
                </td>
                <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">{new Date(job.startedAt).toLocaleString()}</td>
                <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">{job.duration || '-'}</td>
              </tr>
            ))}
            {jobs.length === 0 && (
              <tr>
                <td colSpan={5} className="px-6 py-10 text-center text-sm text-gray-500">No jobs found</td>
              </tr>
            )}
          </tbody>
        </table>
      </div>
    </div>
  );
}
