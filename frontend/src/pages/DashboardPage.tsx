import { useQuery } from '@tanstack/react-query'
import { Badge } from '@/components/ui/badge'
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/ui/card'
import { Header } from '@/components/layout/Header'
import { api } from '@/lib/api'

export function DashboardPage() {
  const healthQuery = useQuery({ queryKey: ['health'], queryFn: api.getHealth })
  const statsQuery = useQuery({ queryKey: ['job-statistics'], queryFn: api.getJobStatistics })
  const toolsQuery = useQuery({ queryKey: ['tools'], queryFn: api.getTools })

  const health = healthQuery.data
  const stats = statsQuery.data
  const toolCount = toolsQuery.data?.length ?? 0

  return (
    <>
      <Header
        title="Dashboard"
        description="Platform runtime overview for the DevOps Toolbox."
      />
      <div className="grid flex-1 gap-6 p-8 md:grid-cols-2 xl:grid-cols-4">
        <Card>
          <CardHeader>
            <CardTitle>Total Jobs</CardTitle>
            <CardDescription>All executions recorded</CardDescription>
          </CardHeader>
          <CardContent className="text-3xl font-semibold">
            {statsQuery.isLoading ? '—' : (stats?.total ?? 0)}
          </CardContent>
        </Card>

        <Card>
          <CardHeader>
            <CardTitle>Running</CardTitle>
            <CardDescription>Currently executing</CardDescription>
          </CardHeader>
          <CardContent className="text-3xl font-semibold">
            {statsQuery.isLoading ? '—' : (stats?.running ?? 0)}
          </CardContent>
        </Card>

        <Card>
          <CardHeader>
            <CardTitle>Completed</CardTitle>
            <CardDescription>Successful executions</CardDescription>
          </CardHeader>
          <CardContent className="text-3xl font-semibold">
            {statsQuery.isLoading ? '—' : (stats?.completed ?? 0)}
          </CardContent>
        </Card>

        <Card>
          <CardHeader>
            <CardTitle>Tools</CardTitle>
            <CardDescription>Discovered via registry</CardDescription>
          </CardHeader>
          <CardContent className="text-3xl font-semibold">
            {toolsQuery.isLoading ? '—' : toolCount}
          </CardContent>
        </Card>

        <Card className="md:col-span-2">
          <CardHeader>
            <CardTitle>System Health</CardTitle>
            <CardDescription>Backend connectivity</CardDescription>
          </CardHeader>
          <CardContent className="space-y-3">
            {healthQuery.isLoading ? (
              <p className="text-sm text-[var(--color-muted-foreground)]">Loading...</p>
            ) : healthQuery.isError ? (
              <Badge variant="danger">Unavailable</Badge>
            ) : health ? (
              <>
                <Badge variant={health.status === 'UP' ? 'success' : 'danger'}>
                  {health.status}
                </Badge>
                <ul className="space-y-1 text-sm">
                  {health.components.map((component) => (
                    <li key={component.name} className="flex justify-between">
                      <span>{component.name}</span>
                      <span>{component.status}</span>
                    </li>
                  ))}
                </ul>
              </>
            ) : (
              <p className="text-sm text-[var(--color-muted-foreground)]">No data available.</p>
            )}
          </CardContent>
        </Card>

        <Card className="md:col-span-2">
          <CardHeader>
            <CardTitle>Job Status Breakdown</CardTitle>
            <CardDescription>Queued and failed jobs</CardDescription>
          </CardHeader>
          <CardContent className="grid grid-cols-2 gap-4 text-sm">
            <div>
              <p className="text-[var(--color-muted-foreground)]">Queued</p>
              <p className="text-2xl font-semibold">{stats?.queued ?? 0}</p>
            </div>
            <div>
              <p className="text-[var(--color-muted-foreground)]">Failed</p>
              <p className="text-2xl font-semibold">{stats?.failed ?? 0}</p>
            </div>
          </CardContent>
        </Card>
      </div>
    </>
  )
}
