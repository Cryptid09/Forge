export function Header({ title, description }: { title: string; description?: string }) {
  return (
    <header className="border-b border-[var(--color-border)] bg-[var(--color-card)] px-8 py-6">
      <h2 className="text-2xl font-semibold tracking-tight">{title}</h2>
      {description ? (
        <p className="mt-1 text-sm text-[var(--color-muted-foreground)]">{description}</p>
      ) : null}
    </header>
  )
}
