import { BrowserRouter, Routes, Route, Link } from 'react-router-dom';
import { Activity, LayoutDashboard, Wrench, ListTodo } from 'lucide-react';
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
    <div className="min-h-screen bg-gray-50 flex">
      <aside className="w-64 bg-white border-r border-gray-200 flex flex-col">
        <div className="h-16 flex items-center px-6 border-b border-gray-200">
          <h1 className="text-xl font-bold text-gray-900 flex items-center gap-2">
            <Activity className="text-blue-600" />
            Forge
          </h1>
        </div>
        <nav className="flex-1 p-4 space-y-1">
          <Link to="/" className="flex items-center gap-3 px-3 py-2 text-gray-700 rounded-md hover:bg-gray-50 hover:text-blue-600 transition-colors">
            <LayoutDashboard size={20} /> Dashboard
          </Link>
          <Link to="/tools" className="flex items-center gap-3 px-3 py-2 text-gray-700 rounded-md hover:bg-gray-50 hover:text-blue-600 transition-colors">
            <Wrench size={20} /> Tools
          </Link>
          <Link to="/jobs" className="flex items-center gap-3 px-3 py-2 text-gray-700 rounded-md hover:bg-gray-50 hover:text-blue-600 transition-colors">
            <ListTodo size={20} /> Jobs
          </Link>
          <Link to="/events" className="flex items-center gap-3 px-3 py-2 text-gray-700 rounded-md hover:bg-gray-50 hover:text-blue-600 transition-colors">
            <Activity size={20} /> Events
          </Link>
          <div className="pt-4 mt-4 border-t border-gray-200">
            <p className="px-3 text-xs font-semibold text-gray-400 uppercase tracking-wider mb-2">Plugins</p>
            <Link to="/docker/containers" className="flex items-center gap-3 px-3 py-2 text-gray-700 rounded-md hover:bg-gray-50 hover:text-blue-600 transition-colors">
              <span className="font-bold text-lg w-5 text-center">🐳</span> Docker
            </Link>
            <Link to="/workflows" className="flex items-center gap-3 px-3 py-2 text-gray-700 rounded-md hover:bg-gray-50 hover:text-blue-600 transition-colors">
              <Activity size={20} className="w-5" /> Workflows
            </Link>
          </div>
          <div className="pt-4 mt-4 border-t border-gray-200">
            <p className="px-3 text-xs font-semibold text-gray-400 uppercase tracking-wider mb-2">Observability</p>
            <Link to="/terminal" className="flex items-center gap-3 px-3 py-2 text-gray-700 rounded-md hover:bg-gray-50 hover:text-blue-600 transition-colors">
              <span className="font-bold text-lg w-5 text-center">💻</span> Terminal
            </Link>
            <Link to="/logs" className="flex items-center gap-3 px-3 py-2 text-gray-700 rounded-md hover:bg-gray-50 hover:text-blue-600 transition-colors">
              <span className="font-bold text-lg w-5 text-center">📜</span> Logs
            </Link>
            <Link to="/analysis" className="flex items-center gap-3 px-3 py-2 text-gray-700 rounded-md hover:bg-gray-50 hover:text-blue-600 transition-colors">
              <span className="font-bold text-lg w-5 text-center">🔍</span> Analysis
            </Link>
          </div>
          <div className="pt-4 mt-4 border-t border-gray-200">
            <p className="px-3 text-xs font-semibold text-gray-400 uppercase tracking-wider mb-2">CI / CD</p>
            <Link to="/git" className="flex items-center gap-3 px-3 py-2 text-gray-700 rounded-md hover:bg-gray-50 hover:text-blue-600 transition-colors">
              <span className="font-bold text-lg w-5 text-center">🌿</span> Git
            </Link>
            <Link to="/build" className="flex items-center gap-3 px-3 py-2 text-gray-700 rounded-md hover:bg-gray-50 hover:text-blue-600 transition-colors">
              <span className="font-bold text-lg w-5 text-center">🔨</span> Build
            </Link>
            <Link to="/artifacts" className="flex items-center gap-3 px-3 py-2 text-gray-700 rounded-md hover:bg-gray-50 hover:text-blue-600 transition-colors">
              <span className="font-bold text-lg w-5 text-center">📦</span> Artifacts
            </Link>
          </div>
          <div className="pt-4 mt-4 border-t border-gray-200">
            <p className="px-3 text-xs font-semibold text-gray-400 uppercase tracking-wider mb-2">Platform</p>
            <Link to="/kafka" className="flex items-center gap-3 px-3 py-2 text-gray-700 rounded-md hover:bg-gray-50 hover:text-blue-600 transition-colors">
              <span className="font-bold text-lg w-5 text-center">📡</span> Kafka
            </Link>
            <Link to="/audit" className="flex items-center gap-3 px-3 py-2 text-gray-700 rounded-md hover:bg-gray-50 hover:text-blue-600 transition-colors">
              <span className="font-bold text-lg w-5 text-center">🛡️</span> Audit
            </Link>
            <Link to="/metrics" className="flex items-center gap-3 px-3 py-2 text-gray-700 rounded-md hover:bg-gray-50 hover:text-blue-600 transition-colors">
              <span className="font-bold text-lg w-5 text-center">📊</span> Metrics
            </Link>
            <Link to="/notifications" className="flex items-center gap-3 px-3 py-2 text-gray-700 rounded-md hover:bg-gray-50 hover:text-blue-600 transition-colors">
              <span className="font-bold text-lg w-5 text-center">🔔</span> Notifications
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
