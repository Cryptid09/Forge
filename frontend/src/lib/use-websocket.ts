import { useQueryClient } from '@tanstack/react-query'
import { useEffect, useRef } from 'react'

const WS_URL =
  import.meta.env.VITE_WS_URL ??
  `${window.location.protocol === 'https:' ? 'wss' : 'ws'}://${window.location.host}/ws`

export function useWebSocket() {
  const queryClient = useQueryClient()
  const socketRef = useRef<WebSocket | null>(null)

  useEffect(() => {
    const socket = new WebSocket(WS_URL)
    socketRef.current = socket

    socket.onmessage = () => {
      queryClient.invalidateQueries({ queryKey: ['jobs'] })
      queryClient.invalidateQueries({ queryKey: ['events'] })
      queryClient.invalidateQueries({ queryKey: ['job-statistics'] })
    }

    socket.onerror = () => {
      // Polling fallback remains active on pages.
    }

    return () => {
      socket.close()
      socketRef.current = null
    }
  }, [queryClient])
}
