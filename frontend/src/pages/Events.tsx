import { useQuery } from '@tanstack/react-query';
import { fetchEvents } from '../api/apiClient';
import { useWebSocket } from '../hooks/useWebSocket';
import { useEffect, useState } from 'react';
import { Event } from '../api/apiClient';

export default function Events() {
  const { data: initialEvents = [], isLoading } = useQuery({ queryKey: ['events'], queryFn: fetchEvents });
  const { events: liveEvents } = useWebSocket('ws://localhost:8080/ws/events');
  const [allEvents, setAllEvents] = useState<Event[]>([]);

  useEffect(() => {
    // Combine initial and live events, avoiding duplicates by ID, sorting by timestamp desc
    const eventMap = new Map<string, Event>();
    initialEvents.forEach(e => eventMap.set(e.id, e));
    liveEvents.forEach(e => eventMap.set(e.id, e));
    
    const combined = Array.from(eventMap.values())
      .sort((a, b) => new Date(b.timestamp).getTime() - new Date(a.timestamp).getTime());
    
    setAllEvents(combined);
  }, [initialEvents, liveEvents]);

  if (isLoading) return <div>Loading events...</div>;

  return (
    <div className="space-y-6">
      <div className="flex items-center gap-4">
        <h2 className="text-2xl font-bold text-gray-900">Event Timeline</h2>
        <div className="flex items-center gap-2">
          <span className="relative flex h-3 w-3">
            <span className="animate-ping absolute inline-flex h-full w-full rounded-full bg-green-400 opacity-75"></span>
            <span className="relative inline-flex rounded-full h-3 w-3 bg-green-500"></span>
          </span>
          <span className="text-sm text-gray-500">Live Updates</span>
        </div>
      </div>
      
      <div className="bg-white rounded-xl shadow-sm border border-gray-100 p-6">
        <div className="relative border-l-2 border-gray-200 ml-3 space-y-8">
          {allEvents.map(event => (
            <div key={event.id} className="relative pl-6">
              <div className="absolute w-3 h-3 bg-blue-500 rounded-full -left-[7px] top-1.5 border-2 border-white"></div>
              <div>
                <p className="text-sm font-semibold text-gray-900">{event.eventType}</p>
                <p className="text-xs text-gray-500 mt-1">{new Date(event.timestamp).toLocaleString()} - Job: {event.jobId.substring(0, 8)}...</p>
                <p className="text-sm text-gray-700 mt-2 bg-gray-50 p-3 rounded border border-gray-100">{event.payload}</p>
              </div>
            </div>
          ))}
          {allEvents.length === 0 && (
            <div className="pl-6 text-gray-500 text-sm">No events found</div>
          )}
        </div>
      </div>
    </div>
  );
}
