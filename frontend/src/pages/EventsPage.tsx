import { useQuery } from '@tanstack/react-query'
import { Badge } from '@/components/ui/badge'
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/ui/card'
import { Header } from '@/components/layout/Header'
import { Loader } from '@/components/ui/Loader'
import { api } from '@/lib/api'
import { useWebSocket } from '@/lib/use-websocket'

function eventLabel(eventType: string) {
  return eventType
    .split('_')
    .map((part) => part.charAt(0) + part.slice(1).toLowerCase())
    .join(' ')
}

export function EventsPage() {
  useWebSocket()

  const eventsQuery = useQuery({
    queryKey: ['events'],
    queryFn: api.getEvents,
    refetchInterval: 5000,
  })

  return (
    <>
      <Header
        title="Events"
        description="Lifecycle event timeline for tool and job executions."
      />
      <div className="p-8">
        <Card>
          <CardHeader>
            <CardTitle>Event Timeline</CardTitle>
            <CardDescription>Published through the internal event bus.</CardDescription>
          </CardHeader>
          <CardContent className="space-y-3">
            {eventsQuery.isLoading ? (
              <Loader label="Loading events..." />
            ) : eventsQuery.isError ? (
              <p className="text-sm text-red-600">Failed to load events.</p>
            ) : eventsQuery.data?.length === 0 ? (
              <p className="text-sm text-(--color-muted-foreground)">No events yet.</p>
            ) : (
              eventsQuery.data?.map((event) => (
                <div
                  key={event.id}
                  className="flex flex-col gap-1 rounded-md border border-(--color-border) p-4 sm:flex-row sm:items-center sm:justify-between"
                >
                  <div>
                    <div className="flex items-center gap-2">
                      <Badge variant="outline">{eventLabel(event.eventType)}</Badge>
                      <span className="text-xs text-(--color-muted-foreground)">
                        {event.toolId}
                      </span>
                    </div>
                    {event.payload ? (
                      <p className="mt-1 text-sm text-(--color-muted-foreground)">
                        {event.payload}
                      </p>
                    ) : null}
                  </div>
                  <div className="text-xs text-(--color-muted-foreground)">
                    <div>{new Date(event.timestamp).toLocaleString()}</div>
                    <div className="font-mono">job {event.jobId.slice(0, 8)}...</div>
                  </div>
                </div>
              ))
            )}
          </CardContent>
        </Card>
      </div>
    </>
  )
}
