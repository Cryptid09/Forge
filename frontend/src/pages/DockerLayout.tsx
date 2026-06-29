import { Link, useLocation } from 'react-router-dom';
import { FaDocker } from 'react-icons/fa';

export default function DockerLayout({ children }: { children: React.ReactNode }) {
  const location = useLocation();
  const path = location.pathname;

  return (
    <div className="space-y-6">
      <div className="rounded-2xl border border-slate-200 bg-gradient-to-r from-sky-50 via-cyan-50 to-white p-6 shadow-sm">
        <div className="flex items-center gap-4">
          <div className="flex h-14 w-14 items-center justify-center rounded-2xl bg-white shadow-sm ring-1 ring-slate-200">
            <FaDocker className="h-8 w-8 text-[#0db7ed]" />
          </div>
          <div>
            <h2 className="text-2xl font-bold text-slate-900">Docker Management</h2>
            <p className="mt-1 text-sm text-slate-600">Container and image control with live operations.</p>
          </div>
        </div>
      </div>

      <div className="border-b border-gray-200">
        <nav className="-mb-px flex space-x-8">
          <Link
            to="/docker/containers"
            className={`${
              path.includes('/docker/containers')
                ? 'border-blue-500 text-blue-600'
                : 'border-transparent text-gray-500 hover:border-gray-300 hover:text-gray-700'
            } whitespace-nowrap py-4 px-1 border-b-2 font-medium text-sm`}
          >
            Containers
          </Link>
          <Link
            to="/docker/images"
            className={`${
              path.includes('/docker/images')
                ? 'border-blue-500 text-blue-600'
                : 'border-transparent text-gray-500 hover:border-gray-300 hover:text-gray-700'
            } whitespace-nowrap py-4 px-1 border-b-2 font-medium text-sm`}
          >
            Images
          </Link>
        </nav>
      </div>

      <div className="pt-4">
        {children}
      </div>
    </div>
  );
}
