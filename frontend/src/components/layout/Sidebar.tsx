import {
  Activity,
  Briefcase,
  FolderKanban,
  LayoutDashboard,
  Settings,
  Wrench,
  Workflow,
} from 'lucide-react'
import { NavLink } from 'react-router-dom'
import { cn } from '@/lib/utils'

const navItems = [
  { to: '/', label: 'Dashboard', icon: LayoutDashboard, end: true },
  { to: '/projects', label: 'Projects', icon: FolderKanban },
  { to: '/tools', label: 'Tools', icon: Wrench },
  { to: '/workflows', label: 'Workflows', icon: Workflow },
  { to: '/jobs', label: 'Jobs', icon: Briefcase },
  { to: '/events', label: 'Events', icon: Activity },
  { to: '/settings', label: 'Settings', icon: Settings },
]

export function Sidebar() {
  return (
    <aside className="flex w-64 flex-col border-r border-[var(--color-border)] bg-[var(--color-sidebar)]">
      <div className="border-b border-[var(--color-border)] px-6 py-5">
        <p className="text-xs font-semibold uppercase tracking-wider text-[var(--color-muted-foreground)]">
          DevOps Toolbox
        </p>
        <h1 className="mt-1 text-lg font-semibold text-[var(--color-sidebar-foreground)]">
          Control Plane
        </h1>
      </div>
      <nav className="flex flex-1 flex-col gap-1 p-3">
        {navItems.map(({ to, label, icon: Icon, end }) => (
          <NavLink
            key={to}
            to={to}
            end={end}
            className={({ isActive }) =>
              cn(
                'flex items-center gap-3 rounded-md px-3 py-2 text-sm font-medium transition-colors',
                isActive
                  ? 'bg-[var(--color-primary)] text-[var(--color-primary-foreground)]'
                  : 'text-[var(--color-sidebar-foreground)] hover:bg-[var(--color-sidebar-accent)]',
              )
            }
          >
            <Icon className="h-4 w-4" />
            {label}
          </NavLink>
        ))}
      </nav>
    </aside>
  )
}
