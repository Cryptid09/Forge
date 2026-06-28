import { Header } from '@/components/layout/Header'
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/ui/card'

export function WorkflowsPage() {
  return (
    <>
      <Header
        title="Workflows"
        description="Workflow orchestration will be available in a future phase."
      />
      <div className="p-8">
        <Card>
          <CardHeader>
            <CardTitle>Workflow engine placeholder</CardTitle>
            <CardDescription>Design and run automation workflows from this view.</CardDescription>
          </CardHeader>
          <CardContent className="text-sm text-[var(--color-muted-foreground)]">
            Workflow definitions, triggers, and execution history will be added after the foundation
            phase is complete.
          </CardContent>
        </Card>
      </div>
    </>
  )
}
