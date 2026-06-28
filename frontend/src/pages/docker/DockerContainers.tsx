import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { fetchContainers, startContainer, stopContainer, restartContainer, removeContainer } from '../../api/dockerClient';
import DockerLayout from '../DockerLayout';
import { Play, Square, RotateCw, Trash2, Terminal } from 'lucide-react';
import { Link } from 'react-router-dom';

export default function DockerContainers() {
  const queryClient = useQueryClient();
  const { data: containers = [], isLoading } = useQuery({ queryKey: ['docker-containers'], queryFn: fetchContainers, refetchInterval: 5000 });

  const invalidate = () => queryClient.invalidateQueries({ queryKey: ['docker-containers'] });

  const startMut = useMutation({ mutationFn: startContainer, onSuccess: invalidate });
  const stopMut = useMutation({ mutationFn: stopContainer, onSuccess: invalidate });
  const restartMut = useMutation({ mutationFn: restartContainer, onSuccess: invalidate });
  const removeMut = useMutation({ mutationFn: removeContainer, onSuccess: invalidate });

  if (isLoading) return <DockerLayout>Loading containers...</DockerLayout>;

  return (
    <DockerLayout>
      <div className="bg-white rounded-xl shadow-sm border border-gray-100 overflow-hidden">
        <table className="min-w-full divide-y divide-gray-200">
          <thead className="bg-gray-50">
            <tr>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Name</th>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Image</th>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Status</th>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Ports</th>
              <th className="px-6 py-3 text-right text-xs font-medium text-gray-500 uppercase">Actions</th>
            </tr>
          </thead>
          <tbody className="bg-white divide-y divide-gray-200">
            {containers.map(c => {
              const name = c.Names && c.Names.length > 0 ? c.Names[0].replace('/', '') : c.Id.substring(0, 12);
              const isRunning = c.State === 'running';
              return (
                <tr key={c.Id}>
                  <td className="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">{name}</td>
                  <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500 truncate max-w-[200px]" title={c.Image}>{c.Image}</td>
                  <td className="px-6 py-4 whitespace-nowrap">
                    <span className={`px-2 inline-flex text-xs leading-5 font-semibold rounded-full 
                      ${isRunning ? 'bg-green-100 text-green-800' : 'bg-gray-100 text-gray-800'}`}>
                      {c.Status}
                    </span>
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                    {c.Ports?.map(p => p.PublicPort ? `${p.PublicPort}->${p.PrivatePort}` : p.PrivatePort).join(', ')}
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap text-right text-sm font-medium space-x-2">
                    {isRunning ? (
                      <button onClick={() => stopMut.mutate(c.Id)} className="text-yellow-600 hover:text-yellow-900" title="Stop"><Square size={18} /></button>
                    ) : (
                      <button onClick={() => startMut.mutate(c.Id)} className="text-green-600 hover:text-green-900" title="Start"><Play size={18} /></button>
                    )}
                    <button onClick={() => restartMut.mutate(c.Id)} className="text-blue-600 hover:text-blue-900" title="Restart"><RotateCw size={18} /></button>
                    <Link to={`/docker/logs/${c.Id}`} className="text-gray-600 hover:text-gray-900 inline-block align-top" title="Logs"><Terminal size={18} /></Link>
                    <button onClick={() => removeMut.mutate(c.Id)} className="text-red-600 hover:text-red-900 ml-2" title="Remove"><Trash2 size={18} /></button>
                  </td>
                </tr>
              );
            })}
            {containers.length === 0 && (
              <tr>
                <td colSpan={5} className="px-6 py-10 text-center text-sm text-gray-500">No containers found</td>
              </tr>
            )}
          </tbody>
        </table>
      </div>
    </DockerLayout>
  );
}
