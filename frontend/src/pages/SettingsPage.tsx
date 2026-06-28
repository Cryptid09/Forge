import { Header } from '@/components/layout/Header'
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/ui/card'

export function SettingsPage() {
  return (
    <>
      <Header
        title="Settings"
        description="Application configuration and preferences."
      />
      <div className="p-8">
        <Card>
          <CardHeader>
            <CardTitle>Foundation settings</CardTitle>
            <CardDescription>Authentication and advanced settings are not enabled in Phase 0.</CardDescription>
          </CardHeader>
          <CardContent className="space-y-2 text-sm text-[var(--color-muted-foreground)]">
            <p>Security is configured to permit all requests during the foundation phase.</p>
            <p>User management, API tokens, and integration credentials will be added later.</p>
          </CardContent>
        </Card>
      </div>
    </>
  )
}
