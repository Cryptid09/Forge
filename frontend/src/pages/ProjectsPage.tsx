import { Header } from '@/components/layout/Header'
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/ui/card'

export function ProjectsPage() {
  return (
    <>
      <Header
        title="Projects"
        description="Project workspaces will be managed here in a future phase."
      />
      <div className="p-8">
        <Card>
          <CardHeader>
            <CardTitle>Coming soon</CardTitle>
            <CardDescription>Placeholder for project management features.</CardDescription>
          </CardHeader>
          <CardContent className="text-sm text-[var(--color-muted-foreground)]">
            No projects are configured yet. This area will host project onboarding, metadata, and
            environment mappings.
          </CardContent>
        </Card>
      </div>
    </>
  )
}
