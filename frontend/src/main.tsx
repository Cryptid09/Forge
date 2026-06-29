import { QueryClientProvider } from '@tanstack/react-query'
import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import { BrowserRouter } from 'react-router-dom'
import App from './App.tsx'
import './index.css'
import { queryClient } from '@/lib/query-client'
import { LoaderProvider } from './lib/LoaderContext'

createRoot(document.getElementById('root')!).render(
  <StrictMode>
    <QueryClientProvider client={queryClient}>
      <LoaderProvider>
        <BrowserRouter>
          <App />
        </BrowserRouter>
      </LoaderProvider>
    </QueryClientProvider>
  </StrictMode>,
)
