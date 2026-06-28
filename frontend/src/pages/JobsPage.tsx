import { useQuery } from '@tanstack/react-query'
import { Badge } from '@/components/ui/badge'
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/ui/card'
import { Header } from '@/components/layout/Header'
import { api } from '@/lib/api'
import { useWebSocket } from '@/lib/use-websocket'

function statusVariant(status: string) {
  switch (status) {
    case 'COMPLETED':
      return 'success' as const
    case 'FAILED':
      return 'danger' as const
    case 'RUNNING':
      return 'default' as const
    default:
      return 'secondary' as const
  }
}

function formatDuration(durationMs: number | null) {
  if (durationMs == null) return '—'
  if (durationMs < 1000) return `${durationMs}ms`
  return `${(durationMs / 1000).toFixed(1)}s`
}

export function JobsPage() {
  useWebSocket()

  const jobsQuery = useQuery({
    queryKey: ['jobs'],
    queryFn: api.getJobs,
    refetchInterval: 5000,
  })

  return (
    <>
      <Header
        title="Jobs"
        description="Execution history for all tool runs. Updates via polling and WebSocket."
      />
      <div className="p-8">
        <Card>
          <CardHeader>
            <CardTitle>Job History</CardTitle>
            <CardDescription>Every tool execution creates a tracked job.</CardDescription>
          </CardHeader>
          <CardContent>
            {jobsQuery.isLoading ? (
              <p className="text-sm text-[var(--color-muted-foreground)]">Loading jobs...</p>
            ) : jobsQuery.isError ? (
              <p className="text-sm text-red-600">Failed to load jobs.</p>
            ) : jobsQuery.data?.length === 0 ? (
              <p className="text-sm text-[var(--color-muted-foreground)]">No jobs yet. Execute a tool to get started.</p>
            ) : (
              <div className="overflow-x-auto">
                <table className="w-full text-left text-sm">
                  <thead>
                    <tr className="border-b border-[var(--color-border)] text-[var(--color-muted-foreground)]">
                      <th className="pb-2 pr-4 font-medium">Job ID</th>
                      <th className="pb-2 pr-4 font-medium">Tool</th>
                      <th className="pb-2 pr-4 font-medium">Status</th>
                      <th className="pb-2 pr-4 font-medium">Started</th>
                      <th className="pb-2 font-medium">Duration</th>
                    </tr>
                  </thead>
                  <tbody>
                    {jobsQuery.data?.map((job) => (
                      <tr key={job.id} className="border-b border-[var(--color-border)] last:border-0">
                        <td className="py-3 pr-4 font-mono text-xs">{job.id.slice(0, 8)}...</td>
                        <td className="py-3 pr-4">{job.toolId}</td>
                        <td className="py-3 pr-4">
                          <Badge variant={statusVariant(job.status)}>{job.status}</Badge>
                        </td>
                        <td className="py-3 pr-4">
                          {job.startedAt ? new Date(job.startedAt).toLocaleString() : '—'}
                        </td>
                        <td className="py-3">{formatDuration(job.durationMs)}</td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
            )}
          </CardContent>
        </Card>
      </div>
    </>
  )
}
