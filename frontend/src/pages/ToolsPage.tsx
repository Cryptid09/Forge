import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query'
import { useState } from 'react'
import { Badge } from '@/components/ui/badge'
import { Button } from '@/components/ui/button'
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/ui/card'
import { Header } from '@/components/layout/Header'
import { api, type ToolDescriptor } from '@/lib/api'

function ExecuteDialog({
  tool,
  onClose,
}: {
  tool: ToolDescriptor
  onClose: () => void
}) {
  const queryClient = useQueryClient()
  const [message, setMessage] = useState('Hello DevOps')
  const [durationSeconds, setDurationSeconds] = useState('5')

  const executeMutation = useMutation({
    mutationFn: () => {
      if (tool.id === 'echo') {
        return api.executeTool(tool.id, { message })
      }
      return api.executeTool(tool.id, { durationSeconds })
    },
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['jobs'] })
      queryClient.invalidateQueries({ queryKey: ['events'] })
      queryClient.invalidateQueries({ queryKey: ['job-statistics'] })
      onClose()
    },
  })

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center bg-black/40 p-4">
      <Card className="w-full max-w-md">
        <CardHeader>
          <CardTitle>Execute {tool.name}</CardTitle>
          <CardDescription>{tool.description}</CardDescription>
        </CardHeader>
        <CardContent className="space-y-4">
          {tool.id === 'echo' ? (
            <label className="block space-y-1 text-sm">
              <span className="text-[var(--color-muted-foreground)]">Message</span>
              <input
                className="w-full rounded-md border border-[var(--color-border)] bg-transparent px-3 py-2"
                value={message}
                onChange={(event) => setMessage(event.target.value)}
              />
            </label>
          ) : (
            <label className="block space-y-1 text-sm">
              <span className="text-[var(--color-muted-foreground)]">Duration (seconds)</span>
              <input
                type="number"
                min={0}
                max={300}
                className="w-full rounded-md border border-[var(--color-border)] bg-transparent px-3 py-2"
                value={durationSeconds}
                onChange={(event) => setDurationSeconds(event.target.value)}
              />
            </label>
          )}
          {executeMutation.isError ? (
            <p className="text-sm text-red-600">Execution failed. Check backend logs.</p>
          ) : null}
          <div className="flex justify-end gap-2">
            <Button variant="outline" onClick={onClose}>
              Cancel
            </Button>
            <Button onClick={() => executeMutation.mutate()} disabled={executeMutation.isPending}>
              {executeMutation.isPending ? 'Starting...' : 'Execute'}
            </Button>
          </div>
        </CardContent>
      </Card>
    </div>
  )
}

export function ToolsPage() {
  const toolsQuery = useQuery({ queryKey: ['tools'], queryFn: api.getTools })
  const [selectedTool, setSelectedTool] = useState<ToolDescriptor | null>(null)

  return (
    <>
      <Header
        title="Tools"
        description="Discovered platform tools. Each tool uses the same execution pipeline."
      />
      <div className="grid gap-4 p-8 md:grid-cols-2">
        {toolsQuery.isLoading ? (
          <p className="text-sm text-[var(--color-muted-foreground)]">Loading tools...</p>
        ) : toolsQuery.isError ? (
          <p className="text-sm text-red-600">Failed to load tools.</p>
        ) : (
          toolsQuery.data?.map((tool) => (
            <Card key={tool.id}>
              <CardHeader>
                <div className="flex items-start justify-between gap-2">
                  <CardTitle>{tool.name}</CardTitle>
                  <Badge variant="secondary">{tool.category}</Badge>
                </div>
                <CardDescription>{tool.description}</CardDescription>
              </CardHeader>
              <CardContent className="flex items-center justify-between">
                <span className="text-xs text-[var(--color-muted-foreground)]">
                  {tool.id} · v{tool.version}
                </span>
                <Button size="sm" onClick={() => setSelectedTool(tool)}>
                  Execute
                </Button>
              </CardContent>
            </Card>
          ))
        )}
      </div>
      {selectedTool ? (
        <ExecuteDialog tool={selectedTool} onClose={() => setSelectedTool(null)} />
      ) : null}
    </>
  )
}
