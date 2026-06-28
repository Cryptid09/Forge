import { Link, useLocation } from 'react-router-dom';

export default function DockerLayout({ children }: { children: React.ReactNode }) {
  const location = useLocation();
  const path = location.pathname;

  return (
    <div className="space-y-6">
      <div className="flex items-center gap-4">
        <h2 className="text-2xl font-bold text-gray-900 flex items-center gap-2">
          <span className="text-blue-600">🐳</span> Docker Management
        </h2>
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
