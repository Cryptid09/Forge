import React, { useState } from 'react';
import { useMutation } from '@tanstack/react-query';
import { logClient, AnalysisResult } from '../../api/logClient';
import { Activity, AlertTriangle, Bug, Info, Play } from 'lucide-react';

export const LogAnalyzerPage: React.FC = () => {
    const [rawLogs, setRawLogs] = useState('');

    const mutation = useMutation({
        mutationFn: () => logClient.analyze(rawLogs),
    });

    const result: AnalysisResult | undefined = mutation.data?.analysis;

    return (
        <div className="flex flex-col h-full p-6 bg-gray-50">
            <h1 className="text-2xl font-bold text-gray-900 mb-6">Log Analyzer</h1>

            <div className="grid grid-cols-2 gap-6 flex-1">
                <div className="flex flex-col bg-white border border-gray-200 rounded shadow-sm p-4">
                    <div className="flex justify-between items-center mb-2">
                        <label className="font-semibold text-gray-700">Paste Logs to Analyze</label>
                        <button 
                            onClick={() => mutation.mutate()} 
                            disabled={!rawLogs.trim() || mutation.isPending}
                            className="bg-indigo-600 hover:bg-indigo-700 text-white px-3 py-1 rounded flex items-center space-x-1 disabled:opacity-50"
                        >
                            <Play size={16} /> <span>{mutation.isPending ? 'Analyzing...' : 'Analyze'}</span>
                        </button>
                    </div>
                    <textarea
                        value={rawLogs}
                        onChange={(e) => setRawLogs(e.target.value)}
                        className="flex-1 border border-gray-300 rounded p-3 font-mono text-sm resize-none focus:outline-none focus:border-indigo-500"
                        placeholder="Paste stack traces, Docker logs, or application logs here..."
                    />
                </div>

                <div className="flex flex-col bg-white border border-gray-200 rounded shadow-sm overflow-y-auto">
                    {!result ? (
                        <div className="flex flex-col items-center justify-center h-full text-gray-500 space-y-4">
                            <Activity size={48} className="text-gray-300" />
                            <p>Submit logs to see the structured analysis.</p>
                        </div>
                    ) : (
                        <div className="p-6 space-y-6">
                            <div className="flex items-start justify-between">
                                <div>
                                    <h2 className="text-xl font-bold text-gray-900">Analysis Summary</h2>
                                    <p className="text-gray-600 mt-1">{result.summary}</p>
                                </div>
                                <div className={`px-3 py-1 rounded font-bold ${
                                    result.severity === 'CRITICAL' || result.severity === 'HIGH' ? 'bg-red-100 text-red-800' :
                                    result.severity === 'MEDIUM' ? 'bg-orange-100 text-orange-800' : 'bg-green-100 text-green-800'
                                }`}>
                                    {result.severity}
                                </div>
                            </div>

                            <div className="bg-indigo-50 border border-indigo-100 p-4 rounded text-indigo-900">
                                <h3 className="font-semibold flex items-center space-x-2"><Bug size={18} /> <span>Suggested Root Cause</span></h3>
                                <p className="mt-1">{result.suggestedRootCause}</p>
                            </div>

                            <div className="grid grid-cols-2 gap-4">
                                <div className="border border-gray-200 p-4 rounded text-center">
                                    <div className="text-3xl font-bold text-red-600">{result.errorCount}</div>
                                    <div className="text-sm text-gray-500 font-medium uppercase tracking-wider">Errors</div>
                                </div>
                                <div className="border border-gray-200 p-4 rounded text-center">
                                    <div className="text-3xl font-bold text-orange-500">{result.warnCount}</div>
                                    <div className="text-sm text-gray-500 font-medium uppercase tracking-wider">Warnings</div>
                                </div>
                            </div>

                            {result.findings.length > 0 && (
                                <div>
                                    <h3 className="text-lg font-bold text-gray-900 mb-3 border-b pb-2">Detected Patterns</h3>
                                    <div className="space-y-3">
                                        {result.findings.map((f, i) => (
                                            <div key={i} className="border border-gray-200 rounded p-3 bg-gray-50">
                                                <div className="flex justify-between items-center mb-2">
                                                    <div className="font-semibold text-gray-800 flex items-center space-x-2">
                                                        <AlertTriangle size={16} className={f.severity === 'CRITICAL' || f.severity === 'HIGH' ? 'text-red-500' : 'text-orange-500'} />
                                                        <span>{f.patternName}</span>
                                                    </div>
                                                    <div className="text-sm bg-gray-200 px-2 py-0.5 rounded text-gray-700">
                                                        {f.occurrences} occurrence{f.occurrences > 1 ? 's' : ''}
                                                    </div>
                                                </div>
                                                <div className="font-mono text-xs text-gray-600 bg-gray-100 p-2 rounded border border-gray-200 break-all">
                                                    {f.excerpt}
                                                </div>
                                                <div className="text-xs text-gray-400 mt-2">Line: {f.lineNumber}</div>
                                            </div>
                                        ))}
                                    </div>
                                </div>
                            )}
                        </div>
                    )}
                </div>
            </div>
        </div>
    );
};
