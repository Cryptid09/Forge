import { BrowserRouter, Routes, Route, Link } from 'react-router-dom';
import { Activity, LayoutDashboard, Wrench, ListTodo } from 'lucide-react';
import { FaDocker, FaGitAlt, FaTerminal, FaHammer, FaBoxOpen, FaShieldAlt, FaChartBar, FaBell } from 'react-icons/fa';
import { SiApachekafka } from 'react-icons/si';
import { FiFileText, FiSearch } from 'react-icons/fi';
import Dashboard from './pages/Dashboard';
import Tools from './pages/Tools';
import Jobs from './pages/Jobs';
import Events from './pages/Events';
import DockerContainers from './pages/docker/DockerContainers';
import DockerImages from './pages/docker/DockerImages';
import DockerLogs from './pages/docker/DockerLogs';
import WorkflowList from './pages/workflows/WorkflowList';
import WorkflowExecutionView from './pages/workflows/WorkflowExecutionView';
import { TerminalPage } from './pages/observability/TerminalPage';
import { LogsPage } from './pages/observability/LogsPage';
import { LogAnalyzerPage } from './pages/observability/LogAnalyzerPage';
import { GitPage } from './pages/git/GitPage';
import { BuildPage } from './pages/build/BuildPage';
import { ArtifactsPage } from './pages/build/ArtifactsPage';
import { KafkaPage } from './pages/events/KafkaPage';
import { AuditPage } from './pages/events/AuditPage';
import { MetricsPage } from './pages/events/MetricsPage';
import { NotificationsPage } from './pages/events/NotificationsPage';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';

const queryClient = new QueryClient();

function AppLayout({ children }: { children: React.ReactNode }) {
  return (
    <div className="min-h-screen bg-linear-to-br from-slate-50 via-white to-slate-100 flex">
      <aside className="w-64 bg-white/90 backdrop-blur border-r border-slate-200 flex flex-col shadow-sm">
        <div className="h-16 flex items-center px-6 border-b border-slate-200 bg-linear-to-r from-blue-50 to-cyan-50">
          <h1 className="text-xl font-bold text-slate-900 flex items-center gap-2">
            <img src="/Forge-logo.png" alt="Forge" className="h-8 w-8 rounded-md object-contain" />
            Forge
          </h1>
        </div>
        <nav className="flex-1 p-4 space-y-1">
          <Link to="/" className="flex items-center gap-3 rounded-md px-3 py-2 text-slate-700 transition-colors hover:bg-slate-50 hover:text-blue-600">
            <LayoutDashboard size={18} /> Dashboard
          </Link>
          <Link to="/tools" className="flex items-center gap-3 rounded-md px-3 py-2 text-slate-700 transition-colors hover:bg-slate-50 hover:text-blue-600">
            <Wrench size={18} /> Tools
          </Link>
          <Link to="/jobs" className="flex items-center gap-3 rounded-md px-3 py-2 text-slate-700 transition-colors hover:bg-slate-50 hover:text-blue-600">
            <ListTodo size={18} /> Jobs
          </Link>
          <Link to="/events" className="flex items-center gap-3 rounded-md px-3 py-2 text-slate-700 transition-colors hover:bg-slate-50 hover:text-blue-600">
            <Activity size={18} /> Events
          </Link>
          <div className="pt-4 mt-4 border-t border-slate-200">
            <p className="px-3 text-xs font-semibold uppercase tracking-wider text-slate-400 mb-2">Plugins</p>
            <Link to="/docker/containers" className="flex items-center gap-3 rounded-md px-3 py-2 text-slate-700 transition-colors hover:bg-slate-50 hover:text-blue-600">
              <FaDocker className="h-4 w-4 text-[#0db7ed]" /> Docker
            </Link>
            <Link to="/workflows" className="flex items-center gap-3 rounded-md px-3 py-2 text-slate-700 transition-colors hover:bg-slate-50 hover:text-blue-600">
              <Activity size={18} className="w-4" /> Workflows
            </Link>
          </div>
          <div className="pt-4 mt-4 border-t border-slate-200">
            <p className="px-3 text-xs font-semibold uppercase tracking-wider text-slate-400 mb-2">Observability</p>
            <Link to="/terminal" className="flex items-center gap-3 rounded-md px-3 py-2 text-slate-700 transition-colors hover:bg-slate-50 hover:text-blue-600">
              <FaTerminal className="h-4 w-4" /> Terminal
            </Link>
            <Link to="/logs" className="flex items-center gap-3 rounded-md px-3 py-2 text-slate-700 transition-colors hover:bg-slate-50 hover:text-blue-600">
              <FiFileText className="h-4 w-4 text-slate-600" /> Logs
            </Link>
            <Link to="/analysis" className="flex items-center gap-3 rounded-md px-3 py-2 text-slate-700 transition-colors hover:bg-slate-50 hover:text-blue-600">
              <FiSearch className="h-4 w-4 text-slate-600" /> Analysis
            </Link>
          </div>
          <div className="pt-4 mt-4 border-t border-slate-200">
            <p className="px-3 text-xs font-semibold uppercase tracking-wider text-slate-400 mb-2">CI / CD</p>
            <Link to="/git" className="flex items-center gap-3 rounded-md px-3 py-2 text-slate-700 transition-colors hover:bg-slate-50 hover:text-blue-600">
              <FaGitAlt className="h-4 w-4 text-[#f05032]" /> Git
            </Link>
            <Link to="/build" className="flex items-center gap-3 rounded-md px-3 py-2 text-slate-700 transition-colors hover:bg-slate-50 hover:text-blue-600">
              <FaHammer className="h-4 w-4 text-slate-700" /> Build
            </Link>
            <Link to="/artifacts" className="flex items-center gap-3 rounded-md px-3 py-2 text-slate-700 transition-colors hover:bg-slate-50 hover:text-blue-600">
              <FaBoxOpen className="h-4 w-4 text-slate-700" /> Artifacts
            </Link>
          </div>
          <div className="pt-4 mt-4 border-t border-slate-200">
            <p className="px-3 text-xs font-semibold uppercase tracking-wider text-slate-400 mb-2">Platform</p>
            <Link to="/kafka" className="flex items-center gap-3 rounded-md px-3 py-2 text-slate-700 transition-colors hover:bg-slate-50 hover:text-blue-600">
              <SiApachekafka className="h-4 w-4 text-[#231F20]" /> Kafka
            </Link>
            <Link to="/audit" className="flex items-center gap-3 rounded-md px-3 py-2 text-slate-700 transition-colors hover:bg-slate-50 hover:text-blue-600">
              <FaShieldAlt className="h-4 w-4 text-slate-700" /> Audit
            </Link>
            <Link to="/metrics" className="flex items-center gap-3 rounded-md px-3 py-2 text-slate-700 transition-colors hover:bg-slate-50 hover:text-blue-600">
              <FaChartBar className="h-4 w-4 text-slate-700" /> Metrics
            </Link>
            <Link to="/notifications" className="flex items-center gap-3 rounded-md px-3 py-2 text-slate-700 transition-colors hover:bg-slate-50 hover:text-blue-600">
              <FaBell className="h-4 w-4 text-slate-700" /> Notifications
            </Link>
          </div>
        </nav>
      </aside>
      <main className="flex-1 p-8 overflow-auto">
        {children}
      </main>
    </div>
  );
}

function App() {
  return (
    <QueryClientProvider client={queryClient}>
      <AppLayout>
        <Routes>
          <Route path="/" element={<Dashboard />} />
          <Route path="/tools" element={<Tools />} />
          <Route path="/jobs" element={<Jobs />} />
          <Route path="/events" element={<Events />} />
          <Route path="/docker/containers" element={<DockerContainers />} />
          <Route path="/docker/images" element={<DockerImages />} />
          <Route path="/docker/logs/:id" element={<DockerLogs />} />
          <Route path="/workflows" element={<WorkflowList />} />
          <Route path="/workflows/executions/:id" element={<WorkflowExecutionView />} />
          <Route path="/terminal" element={<TerminalPage />} />
          <Route path="/logs" element={<LogsPage />} />
          <Route path="/analysis" element={<LogAnalyzerPage />} />
          <Route path="/git" element={<GitPage />} />
          <Route path="/build" element={<BuildPage />} />
          <Route path="/artifacts" element={<ArtifactsPage />} />
          <Route path="/kafka" element={<KafkaPage />} />
          <Route path="/audit" element={<AuditPage />} />
          <Route path="/metrics" element={<MetricsPage />} />
          <Route path="/notifications" element={<NotificationsPage />} />
        </Routes>
      </AppLayout>
    </QueryClientProvider>
  );
}

export default App;
