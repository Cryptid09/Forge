import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { fetchImages, pullImage } from '../../api/dockerClient';
import DockerLayout from '../DockerLayout';
import { Download, Trash2 } from 'lucide-react';
import { useState } from 'react';
import { Loader } from '../../components/ui/Loader';

export default function DockerImages() {
  const queryClient = useQueryClient();
  const { data: images = [], isLoading } = useQuery({ queryKey: ['docker-images'], queryFn: fetchImages, refetchInterval: 10000 });
  const [pullTag, setPullTag] = useState('');

  const invalidate = () => queryClient.invalidateQueries({ queryKey: ['docker-images'] });

  const pullMut = useMutation({ 
    mutationFn: pullImage, 
    onSuccess: () => {
      invalidate();
      alert('Image pulled successfully!');
      setPullTag('');
    },
    onError: () => alert('Failed to pull image')
  });

  if (isLoading) return <DockerLayout><Loader label="Loading images..." /></DockerLayout>;

  return (
    <DockerLayout>
      <div className="mb-6 flex gap-4">
        <input 
          type="text" 
          value={pullTag}
          onChange={e => setPullTag(e.target.value)}
          placeholder="nginx:latest" 
          className="border border-gray-300 rounded-md px-4 py-2 flex-1 max-w-md focus:outline-none focus:ring-2 focus:ring-blue-500"
        />
        <button 
          onClick={() => pullTag && pullMut.mutate(pullTag)}
          disabled={!pullTag || pullMut.isPending}
          className="bg-blue-600 text-white px-4 py-2 rounded-md hover:bg-blue-700 disabled:bg-blue-300 flex items-center gap-2"
        >
          <Download size={18} /> Pull Image
        </button>
      </div>

      <div className="bg-white rounded-xl shadow-sm border border-gray-100 overflow-hidden">
        <table className="min-w-full divide-y divide-gray-200">
          <thead className="bg-gray-50">
            <tr>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Tags</th>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Image ID</th>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Size</th>
              <th className="px-6 py-3 text-right text-xs font-medium text-gray-500 uppercase">Actions</th>
            </tr>
          </thead>
          <tbody className="bg-white divide-y divide-gray-200">
            {images.map(img => (
              <tr key={img.Id}>
                <td className="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">
                  {img.RepoTags?.join(', ') || '<none>'}
                </td>
                <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">{img.Id.substring(7, 19)}</td>
                <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                  {(img.Size / 1024 / 1024).toFixed(2)} MB
                </td>
                <td className="px-6 py-4 whitespace-nowrap text-right text-sm font-medium space-x-2">
                  <button className="text-red-600 hover:text-red-900" title="Remove"><Trash2 size={18} /></button>
                </td>
              </tr>
            ))}
            {images.length === 0 && (
              <tr>
                <td colSpan={4} className="px-6 py-10 text-center text-sm text-gray-500">No images found</td>
              </tr>
            )}
          </tbody>
        </table>
      </div>
    </DockerLayout>
  );
}
