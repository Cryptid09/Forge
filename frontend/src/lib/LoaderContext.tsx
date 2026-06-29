import React, { createContext, useContext, useState, ReactNode } from 'react';

interface LoaderContextType {
  isLoading: boolean;
  message: string;
  showLoader: (message?: string) => void;
  hideLoader: () => void;
}

const LoaderContext = createContext<LoaderContextType | undefined>(undefined);

export function LoaderProvider({ children }: { children: ReactNode }) {
  const [isLoading, setIsLoading] = useState(false);
  const [message, setMessage] = useState('');
  const [isInitialBoot, setIsInitialBoot] = useState(true);

  const showLoader = (msg: string = 'Loading...') => {
    setMessage(msg);
    setIsLoading(true);
  };

  const hideLoader = () => {
    setIsLoading(false);
    setMessage('');
    if (isInitialBoot) setIsInitialBoot(false);
  };

  // Automatically hide the initial boot loader after a short delay (e.g. data fetching is done)
  // For a real app, you'd hide this after the main initial queries complete.
  React.useEffect(() => {
    if (isInitialBoot) {
      showLoader('Initializing Forge...');
      const timer = setTimeout(() => {
        hideLoader();
      }, 1500); // Simulate boot time
      return () => clearTimeout(timer);
    }
  }, [isInitialBoot]);

  return (
    <LoaderContext.Provider value={{ isLoading, message, showLoader, hideLoader }}>
      {children}
      {isLoading && (
        <div className={`fixed inset-0 z-50 flex flex-col items-center justify-center bg-white/80 backdrop-blur-sm transition-opacity duration-300 ${isInitialBoot ? 'bg-white' : ''}`}>
          <div className="relative flex flex-col items-center">
            {isInitialBoot && (
              <img src="/Forge-logo.png" alt="Forge Logo" className="w-24 h-24 mb-6 animate-pulse rounded-2xl shadow-xl" />
            )}
            <div className="w-12 h-12 border-4 border-blue-200 border-t-blue-600 rounded-full animate-spin"></div>
            <p className="mt-4 text-lg font-medium text-slate-700 animate-pulse">{message}</p>
          </div>
        </div>
      )}
    </LoaderContext.Provider>
  );
}

export function useLoader() {
  const context = useContext(LoaderContext);
  if (context === undefined) {
    throw new Error('useLoader must be used within a LoaderProvider');
  }
  return context;
}
