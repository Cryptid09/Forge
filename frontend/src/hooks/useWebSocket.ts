import { useEffect, useState, useRef } from 'react';
import { Event } from '../api/apiClient';

export function useWebSocket(url: string) {
    const [events, setEvents] = useState<Event[]>([]);
    const wsRef = useRef<WebSocket | null>(null);

    useEffect(() => {
        const ws = new WebSocket(url);
        wsRef.current = ws;

        ws.onmessage = (message) => {
            try {
                const event: Event = JSON.parse(message.data);
                setEvents(prev => [event, ...prev]);
            } catch (e) {
                console.error("Failed to parse websocket message", e);
            }
        };

        return () => {
            if (wsRef.current) {
                wsRef.current.close();
            }
        };
    }, [url]);

    return { events, setEvents };
}
