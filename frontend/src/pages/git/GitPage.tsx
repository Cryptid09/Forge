import React, { useState } from 'react';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { gitClient } from '../../api/gitClient';
import { GitBranch, Download, RefreshCw, Trash2, Plus } from 'lucide-react';
import { useNavigate } from 'react-router-dom';

export const GitPage: React.FC = () => {
    const queryClient = useQueryClient();
    const navigate = useNavigate();
    const [name, setName] = useState('');
    const [remoteUrl, setRemoteUrl] = useState('');
    const [localPath, setLocalPath] = useState('');
    const [checkoutBranch, setCheckoutBranch] = useState('');

    const { data: repos, isLoading } = useQuery({
        queryKey: ['gitRepositories'],
        queryFn: gitClient.listRepositories
    });

    const registerMutation = useMutation({
        mutationFn: () => gitClient.registerRepository(name, remoteUrl, localPath),
        onSuccess: () => {
            queryClient.invalidateQueries({ queryKey: ['gitRepositories'] });
            setName(''); setRemoteUrl(''); setLocalPath('');
        }
    });

    const unregisterMutation = useMutation({
        mutationFn: (id: string) => gitClient.unregisterRepository(id),
        onSuccess: () => queryClient.invalidateQueries({ queryKey: ['gitRepositories'] })
    });

    const cloneMutation = useMutation({
        mutationFn: ({ url, path }: { url: string, path: string }) => gitClient.cloneRepository(url, path),
        onSuccess: (data) => navigate(`/jobs`)
    });

    const pullMutation = useMutation({
        mutationFn: (id: string) => gitClient.pullRepository(id),
        onSuccess: (data) => navigate(`/jobs`)
    });

    const checkoutMutation = useMutation({
        mutationFn: ({ id, branch }: { id: string, branch: string }) => gitClient.checkoutBranch(id, branch),
        onSuccess: (data) => navigate(`/jobs`)
    });

    return (
        <div className="flex flex-col h-full space-y-6">
            <h1 className="text-2xl font-bold text-gray-900 flex items-center space-x-2">
                <GitBranch className="text-indigo-600" />
                <span>Git Repositories</span>
            </h1>

            <div className="bg-white border border-gray-200 rounded shadow-sm p-4 flex items-end space-x-4">
                <div className="flex-1">
                    <label className="block text-sm font-medium text-gray-700">Name</label>
                    <input type="text" value={name} onChange={e => setName(e.target.value)} className="mt-1 block w-full rounded border-gray-300 shadow-sm p-2 border focus:ring-indigo-500 focus:border-indigo-500" placeholder="e.g. My Project" />
                </div>
                <div className="flex-1">
                    <label className="block text-sm font-medium text-gray-700">Remote URL</label>
                    <input type="text" value={remoteUrl} onChange={e => setRemoteUrl(e.target.value)} className="mt-1 block w-full rounded border-gray-300 shadow-sm p-2 border focus:ring-indigo-500 focus:border-indigo-500" placeholder="https://github.com/.../repo.git" />
                </div>
                <div className="flex-1">
                    <label className="block text-sm font-medium text-gray-700">Local Path</label>
                    <input type="text" value={localPath} onChange={e => setLocalPath(e.target.value)} className="mt-1 block w-full rounded border-gray-300 shadow-sm p-2 border focus:ring-indigo-500 focus:border-indigo-500" placeholder="/path/to/local/dir" />
                </div>
                <button 
                    onClick={() => registerMutation.mutate()} 
                    disabled={!name || !remoteUrl || !localPath}
                    className="bg-indigo-600 hover:bg-indigo-700 text-white px-4 py-2 rounded flex items-center space-x-1 disabled:opacity-50"
                >
                    <Plus size={18} /> <span>Register</span>
                </button>
            </div>

            <div className="bg-white shadow-sm border border-gray-200 rounded overflow-hidden">
                <table className="min-w-full divide-y divide-gray-200">
                    <thead className="bg-gray-50">
                        <tr>
                            <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Repository</th>
                            <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Local Path</th>
                            <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Registered</th>
                            <th className="px-6 py-3 text-right text-xs font-medium text-gray-500 uppercase tracking-wider">Actions</th>
                        </tr>
                    </thead>
                    <tbody className="bg-white divide-y divide-gray-200">
                        {repos?.map(repo => (
                            <tr key={repo.id}>
                                <td className="px-6 py-4 whitespace-nowrap">
                                    <div className="text-sm font-medium text-gray-900">{repo.name}</div>
                                    <div className="text-sm text-gray-500">{repo.remoteUrl}</div>
                                </td>
                                <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                                    <code className="bg-gray-100 px-2 py-1 rounded">{repo.localPath}</code>
                                </td>
                                <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                                    {new Date(repo.createdAt).toLocaleString()}
                                </td>
                                <td className="px-6 py-4 whitespace-nowrap text-right text-sm font-medium space-x-2 flex justify-end items-center">
                                    <input 
                                        type="text" 
                                        placeholder="branch..."
                                        className="border border-gray-300 rounded px-2 py-1 text-sm w-32"
                                        onBlur={(e) => setCheckoutBranch(e.target.value)}
                                    />
                                    <button 
                                        onClick={() => checkoutBranch && checkoutMutation.mutate({ id: repo.id, branch: checkoutBranch })}
                                        className="text-indigo-600 hover:text-indigo-900"
                                        title="Checkout Branch"
                                    >
                                        <GitBranch size={18} />
                                    </button>
                                    <button 
                                        onClick={() => cloneMutation.mutate({ url: repo.remoteUrl, path: repo.localPath })}
                                        className="text-blue-600 hover:text-blue-900"
                                        title="Clone Repository"
                                    >
                                        <Download size={18} />
                                    </button>
                                    <button 
                                        onClick={() => pullMutation.mutate(repo.id)}
                                        className="text-green-600 hover:text-green-900"
                                        title="Pull Latest"
                                    >
                                        <RefreshCw size={18} />
                                    </button>
                                    <button 
                                        onClick={() => unregisterMutation.mutate(repo.id)}
                                        className="text-red-600 hover:text-red-900"
                                        title="Unregister"
                                    >
                                        <Trash2 size={18} />
                                    </button>
                                </td>
                            </tr>
                        ))}
                        {repos?.length === 0 && (
                            <tr><td colSpan={4} className="px-6 py-4 text-center text-gray-500">No repositories registered.</td></tr>
                        )}
                    </tbody>
                </table>
            </div>
        </div>
    );
};
